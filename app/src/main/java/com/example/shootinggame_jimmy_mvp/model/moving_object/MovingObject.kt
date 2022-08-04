package com.example.shootinggame_jimmy_mvp.model.moving_object

/**
 * <화면에서 위치를 바꾸면 움직이는 객체들을 위한 추상 클래스>
 *     어떤 객체라고 하더라도 동적 객체는 x,y 좌표를 가지고, 이것이 갱신되며 이동함.
 *     따라서 좌표값과 속도값을 프로퍼티로 가지며, 위치 변경을 위한 positionUpdate() 함수 재정의를 요구
 */
abstract class MovingObject(sx: Float, sy:Float, vx: Float, vy: Float) {
    var x = sx
        protected set
    var y = sy
        protected set

    //속도는 각 하위 클래스에서 사용해야 하므로 protected
    protected val velocityX = vx
    protected val velocityY = vy

    /* 각 자식 클래스마다 위치 변경 방식을 다르게 정의 */
    abstract fun positionUpdate()
}