package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet

import com.example.shootinggame_jimmy_mvp.model.Type

//LightBullet -> 빠르기가 60, 파워가 1인 탄환 객체
class LightBullet(id: Long, sx: Float, sy: Float, vx: Float, vy: Float): Bullet(id, sx, sy, vx, vy) {
    override val type: Type = Type.LIGHT_BULLET
    override val speed: Int = 60
    override val power: Int = 1
}