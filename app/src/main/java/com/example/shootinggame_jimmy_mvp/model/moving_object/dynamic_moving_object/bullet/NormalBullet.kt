package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet

import com.example.shootinggame_jimmy_mvp.model.Type

//NormalBullet -> 빠르기가 30, 파워가 2인 탄환 객체
class NormalBullet (id: Long, sx: Float, sy: Float, vx: Float, vy: Float): Bullet(id, sx, sy, vx, vy) {
    override val type: Type = Type.NORMAL_BULLET
    override val speed: Int = 30
    override val power: Int = 2
}