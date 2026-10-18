# Mod de Serpientes para Minecraft: Documento de diseño

> Estado: borrador v0.1. Lo marcado como **[A AJUSTAR]** son valores de balance provisionales. Lo marcado como **[A DEFINIR]** son decisiones pendientes.

## 1. Resumen

Mod de Minecraft Java centrado en serpientes. Añade una estructura de arena con una estatua de serpientes, tres rondas de mobs, una mascota serpiente de 5 niveles, un set de armadura de escamas con mejora, armas de colmillos, pociones y un nido de serpientes con brújula.

**Bucle principal:** arena → botín (colmillos, ojos, escamas) → crafteo (armas, poción, armadura, trampas) → mascota → repetir la arena por botín raro (armadura de serpiente, casco, mejora, ojo dorado).

**Versión de Minecraft y loader:** [A DEFINIR] (recomendado: versión estable con buen soporte en Fabric o NeoForge).

---

## 2. Ítems y materiales

| Ítem | Origen | Uso |
|---|---|---|
| Escama de serpiente | La mascota las suelta; serpientes pequeñas de la ronda 3; nido | Bloques, escaleras, alfombras, armadura |
| Colmillo de serpiente | Soldados serpiente (R1) y lanceros (R2) | Arma y lanza venenosas, trampas, antídoto |
| Ojo de serpiente | Lanceros (R2) | Poción de bendición reptiliana |
| Ojo de serpiente dorado | Botín de la arena | Necesario para subir la mascota a nivel 5 |
| Huevo de serpiente | Primera victoria en la arena (único) | Mascota |
| Antídoto | Se craftea con colmillos | Poción e ingrediente de la mejora de armadura |
| Plantilla de mejora | Botín de la arena | Mejora de armadura |
| Brújula de la arena | Cofre del nido de serpientes (probabilidad) | Apunta a la arena más cercana |

### Bloques de escamas
- Bloque de escamas, escaleras, losas, alfombra y otros derivados **[A DEFINIR: lista completa]**.
- **Bloque de escamas:** es un bloque de columna (se orienta en el eje x, y o z según cómo se coloque, como un tronco). Textura distinta en los lados y en los extremos (arriba y abajo iguales).
- **Receta del bloque de escamas (decidido):** 9 escamas de serpiente en forma 3x3 dan 1 bloque. Receta inversa (decidido): 1 bloque da 9 escamas, sin forma fija. Recetas de los derivados: **[A DEFINIR]**.
- Las trampas de colmillos solo se pueden colocar sobre bloques de escamas.

### Armas
- **Arma de colmillos:** envenena al golpear.
- **Lanza arrojadiza de colmillos:** funciona como el tridente (se lanza y se recoge). Envenena.
- **Trampa de colmillos:** al pisarla hace daño y envenena. No se rompe. Solo sobre bloques de escamas.

### Pociones
- **Poción de bendición reptiliana:** se hace con ojos de serpiente. Da velocidad y fuerza **[A AJUSTAR: duración]**.
- **Antídoto:** se hace con colmillos. Es poción y también ingrediente de la mejora de armadura.

---

## 3. La arena

### 3.1 Estructura y generación
- Círculo de columnas en ruinas con la estatua de serpientes en el centro.
- El suelo es musgo y vegetación, de modo que parece un mini bioma, pero es parte de la estructura.
- Aparece en casi todos los biomas, **excepto nevados o fríos**.
- Frecuencia de generación: [A AJUSTAR].
- Cofres con botín propio: [A DEFINIR].

### 3.2 Flujo
1. El jugador hace clic derecho en la estatua.
2. Los ojos de la estatua se iluminan y empieza la ronda 1.
3. Se completan 3 rondas.
4. Al ganar, la estatua abre la boca y suelta la recompensa.

### 3.3 Rondas
Cantidad de mobs por ronda: **[A AJUSTAR]**. Jefe final: **[A DEFINIR]** (propuesta para versión futura: serpiente gigante en la ronda 3).

| Ronda | Mobs | Drops |
|---|---|---|
| 1 | Zombies + Soldado serpiente | Colmillos |
| 2 | Mobs de larga distancia (lanceros) con lanza venenosa | Colmillos y ojos |
| 3 | Todos los anteriores + serpientes pequeñas | Escamas |

### 3.4 Condiciones de derrota
- Morir, o **salir de la arena**.
- "Salir" = abandonar el radio del círculo de columnas. Propuesta: aviso de unos segundos antes de contar como derrota **[A AJUSTAR]**.
- **Multijugador:** se mantienen las reglas. Solo se pierde si **todos** los jugadores mueren o salen.

### 3.5 Recompensas
- **Primera victoria (única vez):** huevo de serpiente.
- **Victorias siguientes:** botín por probabilidad.

| Probabilidad | Botín |
|---|---|
| 35% | Bloques de escamas + colmillos + ojos |
| 30% | Plantilla de mejora de armadura (se necesitan 3, una por pieza) |
| 15% | Armadura de la serpiente (para la mascota) |
| 10% | Ojo de serpiente dorado |
| 10% | Casco de serpiente |

**Contador de suerte (propuesta) [A AJUSTAR]:** tras unas 8 victorias sin casco (o sin ojo dorado), la siguiente victoria lo garantiza.

---

## 4. Mobs nuevos

### Soldado serpiente
- Aparece en la ronda 1.
- Mucho empuje (knockback).
- Su arma envenena.
- Suelta colmillos de serpiente.
- Vida, daño y veneno: [A AJUSTAR].

### Lancero serpiente
- Aparece en la ronda 2.
- Ataque a distancia con lanza arrojadiza que envenena.
- Suelta colmillos y ojos de serpiente.
- Vida, daño y alcance: [A AJUSTAR].

### Serpiente pequeña
- Aparece en la ronda 3.
- Suelta escamas.
- Estadísticas: [A AJUSTAR].

---

## 5. Mascota serpiente

### 5.1 Obtención y eclosión
- Se obtiene **una sola vez**, en la primera victoria en la arena. Si la pierdes, no hay forma de recuperarla.
- ¿Una vez por jugador o por mundo? **[A DEFINIR]**
- Nace pequeña.
- **Eclosión (propuesta) [A DEFINIR]:** colocar el huevo sobre bloques de escamas cerca de una fuente de calor (magma o fogata) durante unos minutos.
- **Alimentación (propuesta) [A DEFINIR]:** carne cruda cura poco, cocinada cura más.

### 5.2 Comportamiento
- Modos: seguir, quedarse quieta, patrullar (como otras mascotas).
- Es **inmortal**: no puede morir (ver petrificación).

### 5.3 Niveles

| Nivel | Tamaño | Capacidades |
|---|---|---|
| 1 | Pequeña | Daño mínimo |
| 2 | Crece un poco | Más daño y envenena |
| 3 | [A DEFINIR] | Desbloquea el **sofoco** |
| 4 | [A DEFINIR] | [A DEFINIR] |
| 5 | Mucho más grande | Todos los daños aumentan, más vida, mayor umbral de sofoco, **montable** (con armadura de serpiente). Requiere ojo dorado para subir a este nivel (propuesta) |

Vida, daño, umbral de sofoco y duración por nivel: **[A AJUSTAR]**.

**Subida de nivel (propuesta) [A DEFINIR]:** mezcla de XP, comida y escamas. Los niveles 4 y 5 piden ítems más caros.

### 5.4 Ataques
Alterna **al azar** entre:
1. **Golpe + veneno.**
2. **Sofoco** (desde nivel 3).

### 5.5 Sofoco
- La serpiente enreda al enemigo y le hace daño de sofoco.
- **Mientras sofoca, recibe todo el daño** que le hagan al enemigo.
- Si el daño acumulado supera su **umbral**, suelta al enemigo y **sigue activa** (no se petrifica).
- Si su vida llega a 0, se petrifica.
- Umbral del sofoco: [A AJUSTAR]. En nivel 5 es mayor.

### 5.6 Petrificación
- Cuando la vida llega a 0, en vez de morir **se convierte en piedra**.
- Tras **40 segundos** revive con **la mitad de la vida** [A AJUSTAR].
- **Hibernación:** tras **5 petrificaciones**, al revivir entra en hibernación durante **15 minutos**. Durante ese tiempo no pelea ni se puede montar.
- El contador de petrificaciones se reinicia al terminar la hibernación **[A DEFINIR]**.

### 5.7 Engullir (penalización)
- Se activa si la serpiente **mata un enemigo**, o si el enemigo **muere mientras ella sofoca**.
- Hace una animación de engullir y **recupera vida**.
- Durante la animación **no ayuda al jugador**.
- **No se interrumpe** si la golpean: solo pierde vida.
- Si su vida llega a 0 mientras engulle, **se petrifica** y la animación se cancela.
- Propuesta: recibe más daño mientras traga **[A AJUSTAR]**.

### 5.8 Prioridad de estados
```
Petrificada > Engullendo > Sofocando > Atacando > Seguir / Quieta / Patrullar
```
Hibernación es un estado aparte que anula todos los demás excepto el temporizador.

### 5.9 Escamas y caricias
- La serpiente suelta escamas cada cierto tiempo [A AJUSTAR].
- Acariciarla **reduce el tiempo** hasta la siguiente escama.
- La caricia tiene **cooldown** para evitar farmeo [A AJUSTAR].

### 5.10 Armadura de la serpiente
- Se consigue como botín de la arena (15%).
- La serpiente recibe menos daño en general, así que aguanta más en el sofoco.
- **Junto con el nivel 5**, permite montarla.

### 5.11 Montura
- **Requisitos:** nivel 5 y armadura de serpiente equipada.
- Se controla como un caballo **[A DEFINIR: silla necesaria o no]**.
- Mientras la montas, el jugador pelea con sus propias armas; la serpiente no ataca ni sofoca (propuesta).
- Si se petrifica con el jugador encima, lo baja al suelo.

---

## 6. Armadura de escamas

### 6.1 Crafteo
- Piezas: **peto, pantalón y botas**.
- Receta: escamas + netherite en la mesa de crafteo **[A DEFINIR: receta exacta]**.

### 6.2 Bendición reptiliana (peto + pantalón + botas)
- Requiere las **tres piezas**. El casco no participa.
- Se activa cuando la vida está **por debajo de 3.5 corazones**.
- Efectos: **velocidad y fuerza**.
- Al activarse, una **pequeña explosión** envenena a los mobs cercanos (no afecta a jugadores). **Cooldown: 1 minuto y medio.**
- El efecto continúa mientras sigas por debajo de 3.5 corazones. Al subir por encima, dura **3 segundos más**.
- Valores (potencia, radio, duración del veneno): [A AJUSTAR]. Si resulta muy fuerte, acortar el efecto o quitar la fuerza.

### 6.3 Casco de serpiente
- **No** forma parte de la bendición.
- Te da el poder de la serpiente: al morir te **petrificas** y luego **reviven**.
- **Cooldown: 5 minutos.**
- Solo se consigue como botín de la arena (10%).
- Duración de la petrificación y vida al revivir: [A AJUSTAR].

---

## 7. Mejora de armadura (antídoto)

- **Antídoto:** se craftea con colmillos.
- **Plantilla de mejora:** botín de la arena (30%).
- **Mesa de mejora:** se coloca la pieza de armadura + plantilla + antídoto.
- Se necesita **una mejora por pieza** (peto, pantalón y botas).
- Con el **set completo mejorado**: inmunidad al **veneno** y al **wither**.
- Balance: considerar wither reducido en vez de inmune [A AJUSTAR].
- Efectos extra posibles: resistencia al fuego en el Nether.

---

## 8. Nido de serpientes

- Estructura pequeña y rara con serpientes pasivas alrededor.
- Biomas: desierto, jungla, pantano **[A DEFINIR]**.
- Contiene un **cofre** que puede (o no) tener la **brújula de la arena**.
- Probabilidad de la brújula y resto del botín: [A AJUSTAR].
- La brújula apunta a la arena más cercana.

---

## 9. Máquina de estados de la mascota (resumen)

```
[Huevo] --incubar--> [Nivel 1..5]

Estados activos:
  Seguir / Quieta / Patrullar
        ↓ (enemigo cerca)
  Atacando  --(azar, nivel>=3)--> Sofocando
        ↓                              ↓
   (mata enemigo)               (enemigo muere / ella mata)
        ↓                              ↓
            ----------> Engullendo ----> vuelve a Seguir

  Sofocando: daño acumulado > umbral → suelta al enemigo → Atacando/Seguir
  Cualquier estado: vida = 0 → Petrificada (40 s) → revive con 50% vida
  5 petrificaciones → Hibernación (15 min)
```

---

## 10. Roadmap

| Versión | Contenido |
|---|---|
| **v0.1** | Ítems, bloques, recetas, armadura y efectos |
| **v0.5** | Mobs (soldado, lancero), arena, rondas y botín |
| **v1.0** | Mascota completa, montura, mejora de armadura, nido y brújula |
| **v1.1** | Herramientas de escamas, bloques decorativos, logros (advancements) |
| **v2.0** | Jefe final, dificultad difícil, cosméticos de mascota |

### Ideas futuras (sin priorizar)
- Jefe final: serpiente gigante en la ronda 3.
- Escudo de escamas, arco/ballesta de colmillos.
- Serpientes salvajes domesticables (distintas del huevo único).
- Cosméticos de piel según tipo de escama.
- Sistema de cariño (más caricias = más vínculo).
- Habilidad exclusiva del nivel 5 (rugido o mordida que ralentiza).
- Modo de dificultad de la arena con mejor botín.
- Compatibilidad con JEI/REI.

---

## 11. Decisiones pendientes

1. Versión de Minecraft y loader (Fabric / NeoForge).
2. ¿El huevo único es por jugador o por mundo?
3. Eclosión, alimentación y subida de nivel de la mascota.
4. Capacidades del nivel 4 y tamaño del nivel 3.
5. ¿La montura necesita silla?
6. Cantidad de mobs por ronda y jefe final.
7. Receta exacta de la armadura y lista completa de bloques de escamas.
8. Botín del cofre de la arena y probabilidad de la brújula en el nido.
9. Regla exacta de "salir de la arena" (radio y aviso).
10. Todos los valores de balance marcados como [A AJUSTAR].

---

## 12. Notas para implementación (Claude Code)

- Trabajar por fases en el orden del roadmap, probando el juego entre cada paso.
- Usar Git desde el inicio.
- Implementar la mascota al final, empezando por una máquina de estados simple con la prioridad de la sección 5.8.
- Modelos, texturas y animaciones: hechos a mano en Blockbench.
