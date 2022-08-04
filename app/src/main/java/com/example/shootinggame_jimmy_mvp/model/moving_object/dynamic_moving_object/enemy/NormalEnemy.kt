package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy

import com.example.shootinggame_jimmy_mvp.model.Type

//NormalEnemy -> 빠르기가 20, 체력이 2인 적 객체
class NormalEnemy (id: Long, sx: Float): Enemy(id, sx) {
    override val type: Type = Type.NORMAL_ENEMY
    override val speed: Int = 20
    override var health: Int = 2
}