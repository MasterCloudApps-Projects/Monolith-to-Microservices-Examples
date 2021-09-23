# Parallel Run

En los patrones anteriormente estudiados `Strangler Fig` y `Branch By Abstraction`, teníamos la posibilidad de que coexistiera la versión antigua y nueva de la funcionalidad, pero sólamente una de ellas se activava en un momento concreto.

Este patrón, `Parallel Run` en lugar de llamar a la implementación antigua o nueva, llamamos a ambas, lo que nos permite comparar los resultados para asegurarnos de que sean equivalentes.

![alt text](3.30_parallel_run.png)

Utiliza la técnica de Dark Launching, implementar una nueva funcionalidad pero que sea invisible para los usuarios. Por lo tnato parallel run es una forma de implementar esta técnica, ya que la nueva funcionalidad es invisible.


## **Ejemplo 1. Usando Spies**

![alt text](3.31_parallel_run.png)

Tener una implementación fake en el MS, que no envíe el mail, pero registre "como que se hubiera enviado".

## **Ejemplo 2. Github Scientist**

https://github.com/rawls238/Scientist4J


## **Ejemplo 3. Canary Releasing**
Lanzar una versión Canary para un subconjunto de usuarios, por si se produce algún problema sólo un pequeño grupo de usuarios se verán afectados.
Llamaríamos a ambas implementaciones.



