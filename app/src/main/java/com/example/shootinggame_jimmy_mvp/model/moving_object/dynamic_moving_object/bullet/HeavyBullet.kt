package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet

import com.example.shootinggame_jimmy_mvp.model.Type

//HeavyBullet -> 빠르기가 15, 파워가 4인 탄환 객체
class HeavyBullet (id: Long, sx: Float, sy: Float, vx: Float, vy: Float): Bullet(id, sx, sy, vx, vy) {
    override val type: Type = Type.HEAVY_BULLET
    override val speed: Int = 15
    override val power: Int = 4
}