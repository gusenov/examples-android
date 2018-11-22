package com.example.bouncingobjectapplication;

public class BouncingObject {

    private float x;

    private float y;


    /** Координата начальной точки по X. */
    private float x0;

    /** Координата начальной точки по Y. */
    private float y0;


    /** Время движения. */
    private float time = 0;

    /** Шаговое значение. */
    private float timeStep = 0.8f;


    /**
     * Ускорение прописываем произвольным образом
     * (оптимальное значение определится при тестировании).
     */
    private float acceleration = 2f;


    /**
     * Начальное значение вертикальной составляющей скорости.
     * Будем считать, что объект начинает движение в верхней точке траектории,
     * поэтому вертикальную составляющую начальной скорости заладим равной нулю.
     */
    private float velocityY = 0;

    /** Текущая величина вертикальной составляющей скорости объекта. */
    private float newV = 0;


    /**
     * Начальное значение горизонтальной составляющей скорости.
     * Горизонтальную составляющую начальной скорости прописываем произвольным образом
     * (оптимальное значение определится при тестировании).
     */
    private float velocityX = 6f;


    /** Понижающий коэффициент. */
    private float decreasingCoefficient = 0.85f;


    private float newX = 0;

    private float newY = 0;


    /** «Пол». */
    private float floorY;

    private float wallX;


    private float velocityXStopThreshold;

    private float velocityYStopThreshold;


    public BouncingObject(
            float x0,
            float y0,
            float floorY,
            float wallX,
            float velocityXStopThreshold,
            float velocityYStopThreshold) {

        this.x0 = x = x0;
        this.y0 = y = y0;

        this.floorY = floorY;
        this.wallX = wallX;

        this.velocityXStopThreshold = velocityXStopThreshold;
        this.velocityYStopThreshold = velocityYStopThreshold;
    }

    public BouncingObject(
            float x0,
            float y0,
            float floorY,
            float wallX,
            float velocityXStopThreshold,
            float velocityYStopThreshold,
            float velocityX,
            float velocityY,
            float decreasingCoefficient,
            float acceleration,
            float timeStep) {

        this.x0 = x = newX = x0;
        this.y0 = y = newY = y0;

        this.floorY = floorY;
        this.wallX = wallX;

        this.velocityXStopThreshold = velocityXStopThreshold;
        this.velocityYStopThreshold = velocityYStopThreshold;

        this.velocityX = velocityX;
        this.velocityY = velocityY;

        this.decreasingCoefficient = decreasingCoefficient;

        this.acceleration = acceleration;

        this.timeStep = timeStep;
    }

    public boolean next() {
        boolean isMoved = false;

        // Превышение у-координатой объекта некоторого порогового значения:
        if (y >= floorY) {
            // То, что должно произойти при столкновении объекта с «полом»:

            // Изменение направления движения объекта на языке физики означает
            // инверсию знака вертикальной составляющей скорости.
            // Помимо этого, так как при ударе объект должен потерять часть энергии,
            // вводим понижающий коэффициент:
            velocityY = -newV * decreasingCoefficient;

            // В результате удара должна уменьшиться
            // и горизонтальная (ранее неизменная) составляющая скорости:
            velocityX = decreasingCoefficient * velocityX;

            // Так как была изменена начальная точка, время должно быть обнулено.
            // Но чтобы код не прокрутился вхолостую,
            // присваиваем переменной t ее шаговое значение:
            time = timeStep;

            // Изменение знака скорости — это операция, требующая,
            // чтобы координаты начальной точки (X0, Y0)
            // были заменены координатами точки отскока:
            x0 = newX;
            y0 = newY;
        } else if (y <= 0) {
            velocityY *= -1 * decreasingCoefficient;

            velocityX *= decreasingCoefficient;
            time = timeStep;
            x0 = newX;
            y0 = newY;
        }

        if (x <= 0 || x >= wallX) {
            velocityX *= -1;

            velocityY = velocityYStopThreshold + Float.MIN_VALUE;
            time = timeStep;
            x0 = newX;
            y0 = newY;
        }

        // Параболическая траектория объекта образуется в результате
        // его ускоряемого действием силы тяжести движения по вертикали
        // и перемещения с постоянной скоростью по горизонтали.
        // Эти составляющие взаимонезависимы.
        // Поэтому логичным будет рассматривать базовые движения по отдельности.

        // В механике координата ускоренно движущегося тела определяется по формуле:
        y = y0 + (velocityY * time) + acceleration * time * time / 2.0f;

        // Движение объекта по горизонтали равномерно,
        // поэтому и формула используется более простая:
        x = x0 + velocityX * time;

        // Текущая величина вертикальной составляющей скорости объекта
        // вычисляется по известной физической формуле
        // для скорости тела при ускоренном движении:
        newV = velocityY + acceleration * time;

        // Чтобы объект не проваливался под «пол»:
        if (y > floorY) {
            y = floorY;
        }

        isMoved = Math.abs(velocityX) >= velocityXStopThreshold
                || Math.abs(velocityY) >= velocityYStopThreshold;

        // «Следящие» переменные в теле функции для координат объекта:
        newY = y;
        newX = x;

        // При каждом запуске функции
        // будем прибавлять к переменной t некоторую величину
        // (ее подходящее значение подберем потом эмпирически).
        // Это даст тот же эффект, что и привязка к реальным «часам».
        time += timeStep;

        return isMoved;
    }

    /**
     * Функция определяет различаются ли числа {@paramref a} и {@paramref b}
     * больше чем на значение {@paramref epsilon}.
     *
     * Примеры:
     *
     * a = 0.09, b = 0.05, epsilon = 0.01
     * a - b = 0.09 - 0.05 = 0.04 > 0.01 = true
     *
     * a = 0.09, b = 0.05, epsilon = 0.05
     * a - b = 0.09 - 0.05 = 0.04 > 0.05 = false
     *
     * @param a
     * @param b
     * @param epsilon
     * @return
     */
    private boolean diff(float a, float b, float epsilon) {
        if (a > b) {
            return a - b > epsilon;
        } else if (b > a) {
            return b - a > epsilon;
        }
        return false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }
}
