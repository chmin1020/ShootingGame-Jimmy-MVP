package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy

import com.example.shootinggame_jimmy_mvp.model.Type

//HeavyEnemy -> 빠르기가 10, 체력이 4인 적 객체
class HeavyEnemy(id: Long, sx: Float): Enemy(id, sx) {
    override val type: Type = Type.HEAVY_ENEMY
    override val speed: Int = 10
    override var health: Int = 4
}