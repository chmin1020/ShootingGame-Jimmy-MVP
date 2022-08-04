package com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy

import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.Enemy
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.HeavyEnemy
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.LightEnemy
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.NormalEnemy

/* 타입에 따라 적절한 Enemy 하위 객체를 만들기 위한 공장 클래스 */
object EnemyFactory {
    fun create(type: Type, id: Long, sx: Float): Enemy {
        if(type == Type.LIGHT_ENEMY)
            return LightEnemy(id, sx)
        if(type == Type.NORMAL_ENEMY)
            return NormalEnemy(id, sx)
        return HeavyEnemy(id, sx)
    }
}