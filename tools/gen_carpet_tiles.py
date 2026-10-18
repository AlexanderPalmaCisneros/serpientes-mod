"""Generates the 16 tiles of the snake scale carpet (one per neighbor combination).

Each tile has a texture AND a 3D model made of small boxes that follow the outline, so the
relief (thickness) ends exactly where the design ends.

The scale pattern is copied from snake_scale_block_side.png WITHOUT rotating it, so the
pattern keeps the same direction in every tile. Only the outline changes.
Needs Python 3 + Pillow:  python tools/gen_carpet_tiles.py
"""
import json, math, os, random, sys
from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "src", "main", "resources", "assets", "snakemod")
SIZE = 32       # texture size
D = 3           # base cut depth on open sides; both ends of every side are exactly D so tiles line up
R = 7           # radius of rounded outer corners
OUTLINE = (20, 22, 10, 255)

side = Image.open(f"{ROOT}/textures/block/snake_scale_block_side.png").convert("RGBA")

def depth_fn(seed):
    rnd = random.Random(seed)
    knots = [D] + [rnd.uniform(1, 5.2) for _ in range(5)] + [D]
    out = []
    for t in range(SIZE):
        u = t / (SIZE - 1) * (len(knots) - 1)
        i = min(int(u), len(knots) - 2)
        f = (1 - math.cos((u - i) * math.pi)) / 2
        out.append(int(round(knots[i] * (1 - f) + knots[i + 1] * f)))
    out[0] = out[-1] = D
    return out

DEPTH = {s: depth_fn(f"side-{s}") for s in "NESW"}

def make_tile(nb):  # nb = set of sides that HAVE a neighbor
    open_sides = {s for s in "NESW" if s not in nb}
    d = {s: (DEPTH[s] if s in open_sides else [0] * SIZE) for s in "NESW"}
    inside = [[x >= d["W"][y] and x <= SIZE - 1 - d["E"][y] and y >= d["N"][x] and y <= SIZE - 1 - d["S"][x]
               for x in range(SIZE)] for y in range(SIZE)]
    for (a, b), (cx, cy) in {("N", "W"): (0, 0), ("N", "E"): (1, 0), ("S", "E"): (1, 1), ("S", "W"): (0, 1)}.items():
        if a in open_sides and b in open_sides:
            px = D + R if cx == 0 else SIZE - 1 - D - R
            py = D + R if cy == 0 else SIZE - 1 - D - R
            for y in range(SIZE):
                for x in range(SIZE):
                    if (x < px if cx == 0 else x > px) and (y < py if cy == 0 else y > py) and math.hypot(x - px, y - py) > R:
                        inside[y][x] = False
    img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    for y in range(SIZE):
        for x in range(SIZE):
            if not inside[y][x]:
                continue
            edge = any(0 <= x + dx < SIZE and 0 <= y + dy < SIZE and not inside[y + dy][x + dx]
                       for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)))
            img.putpixel((x, y), OUTLINE if edge else side.getpixel((x, y)))
    return img, inside

def name_of(nb):
    return "".join(s.lower() for s in "NESW" if s in nb) or "none"

def rectangles(inside):
    """Greedy: merge equal horizontal runs of consecutive rows into rectangles (x0, y0, x1, y1), end exclusive."""
    open_rects = {}   # (x0, x1) -> [x0, y0, x1, y1]
    done = []
    for y in range(SIZE):
        runs, x = [], 0
        while x < SIZE:
            if inside[y][x]:
                x0 = x
                while x < SIZE and inside[y][x]: x += 1
                runs.append((x0, x))
            else:
                x += 1
        new_open = {}
        for r in runs:
            if r in open_rects:
                new_open[r] = open_rects.pop(r)
                new_open[r][3] = y + 1
            else:
                new_open[r] = [r[0], y, r[1], y + 1]
        done.extend(open_rects.values())
        open_rects = new_open
    done.extend(open_rects.values())
    return done

def model_for(nm, inside, dark_px):
    """Boxes of 1/16 height following the mask. 1 texture px = 0.5 model units."""
    h = lambda v: v / 2
    def exposed(cells):
        return any(not (0 <= cx < SIZE and 0 <= cy < SIZE and inside[cy][cx]) and not (cx < 0 or cx >= SIZE or cy < 0 or cy >= SIZE) or False for cx, cy in cells)
    ux, uy = h(dark_px[0]), h(dark_px[1])
    dark_uv = [ux, uy, ux + 0.5, uy + 0.5]
    elements = []
    for x0, y0, x1, y1 in rectangles(inside):
        faces = {"up": {"uv": [h(x0), h(y0), h(x1), h(y1)], "texture": "#tex"}}
        # a side face is only emitted if some cell next to it is empty (inside the tile)
        if exposed([(x, y0 - 1) for x in range(x0, x1)]): faces["north"] = {"uv": dark_uv, "texture": "#tex"}
        if exposed([(x, y1) for x in range(x0, x1)]):     faces["south"] = {"uv": dark_uv, "texture": "#tex"}
        if exposed([(x0 - 1, y) for y in range(y0, y1)]): faces["west"] = {"uv": dark_uv, "texture": "#tex"}
        if exposed([(x1, y) for y in range(y0, y1)]):     faces["east"] = {"uv": dark_uv, "texture": "#tex"}
        elements.append({"from": [h(x0), 0, h(y0)], "to": [h(x1), 1, h(y1)], "faces": faces})
    return {"parent": "minecraft:block/thin_block",
            "textures": {"tex": f"snakemod:block/snake_scale_carpet_{nm}", "particle": "#tex"},
            "elements": elements}

if __name__ == "__main__":
    for f in os.listdir(f"{ROOT}/textures/block"):
        if f.startswith("snake_scale_carpet_"): os.remove(f"{ROOT}/textures/block/{f}")
    for f in os.listdir(f"{ROOT}/models/block"):
        if f.startswith("snake_scale_carpet_"): os.remove(f"{ROOT}/models/block/{f}")
    variants = {}
    for n in (False, True):
        for e in (False, True):
            for s_ in (False, True):
                for w in (False, True):
                    nb = {k for k, v in zip("NESW", (n, e, s_, w)) if v}
                    nm = name_of(nb)
                    img, inside = make_tile(nb)
                    img.save(f"{ROOT}/textures/block/snake_scale_carpet_{nm}.png")
                    dark = next((x, y) for y in range(SIZE) for x in range(SIZE) if inside[y][x] and img.getpixel((x, y)) == OUTLINE)
                    json.dump(model_for(nm, inside, dark), open(f"{ROOT}/models/block/snake_scale_carpet_{nm}.json", "w"), indent=1)
                    key = f"north={str(n).lower()},east={str(e).lower()},south={str(s_).lower()},west={str(w).lower()}"
                    variants[key] = {"model": f"snakemod:block/snake_scale_carpet_{nm}"}
    json.dump({"variants": variants}, open(f"{ROOT}/blockstates/snake_scale_carpet.json", "w"), indent=2)
    json.dump({"parent": "snakemod:block/snake_scale_carpet_none"}, open(f"{ROOT}/models/item/snake_scale_carpet.json", "w"), indent=2)
    print("ok")
