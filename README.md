# Tanks: Orchestra of War
## Описание проекта
Это небольшая 2D-аркада c видом сверху, расчитанная на игру по сети с другими людьми. Каждому игроку под управление даётся танк. Цель игры - уничтожить танки других игроков и остаться в живых. Для помощи игрокам на карте периодически появляются специальные ящики-бонусы: 3 вида ящиков для смены оборудования танка и 1 ящик ремонтного набора. 

Ящики для смены оборудования позволяют игроку получить новый корпус, орудие или тип снарядов. В результате получается достаточно большое количество комбинаций, каждая из которых может найти своё применение в зависимости от ситуации. 

скриншот.png

## Управление 
WS - движение вперёд/назад.  
AD - поворот танка по оси.  
ЛКМ - выстрел танка в направлении курсора.  
1/2/3/4 - блокировать подбор ящиков (для корпуса/орудия/типа снаряда/ремонтного набора).  
F3 - информация о танке.  
### Только для синглплеера
N - умереть.  
T - сменить корпус.  
G - сменить орудие.  
B - сменить тип снарядов.  
H - отремонтировать себя (40%).  
F - отремонтировать себя (полностью).  
V - увеличивает эффект вампиризма до максимума.  

## Техническая часть
Игра написана на Java 8.
Для отображения графики используется библиотека LWJGL, позволяющая обращаться к функциям OpenGL.  
Для работы с изображениями, звуками и шрифтами используется библиотека Slick-Util.  
Для работы программы используется самописный [движок](https://github.com/KeyJ148/Engine).
