package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet

import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.DynamicMovingObject

/**
 * <탄환 개체의 base>
 *     기본적으로 화면에서 동적으로 등장하므로 부모 클래스는 DynamicObject.
 *     탄환은 대포의 위치벡터에 따라 시작 위치와 속도가 다르다.
 *     따라서 속도벡터로 2가지(velX, velY)를 받으며, 이들로 위치를 갱신한다.
 */
abstract class Bullet(id: Long, sx: Float, sy: Float, vx: Float, vy: Float): DynamicMovingObject(id, sx, sy, vx, vy) {
    abstract val speed:Int
    abstract val power:Int

    /* 탄환은 x,y 방향으로 모두 속도를 가지고 위치가 변함 */
    override fun positionUpdate() {
        x += velocityX * speed
        y += velocityY * speed
    }
}