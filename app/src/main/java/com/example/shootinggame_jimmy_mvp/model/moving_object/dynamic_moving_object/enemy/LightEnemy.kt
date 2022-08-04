package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy

import com.example.shootinggame_jimmy_mvp.model.Type

//LightEnemy -> 빠르기가 40, 체력이 1인 적 객체
class LightEnemy (id: Long, sx: Float): Enemy(id, sx) {
    override val type: Type = Type.LIGHT_ENEMY
    override val speed: Int = 40
    override var health: Int = 1
}