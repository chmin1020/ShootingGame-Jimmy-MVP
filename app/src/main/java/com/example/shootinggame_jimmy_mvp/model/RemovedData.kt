package com.example.shootinggame_jimmy_mvp.model

import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.Bullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.Enemy

/**
 *  지워진 탄환과 적 목록을 반환할 때 사용할 데이터 클래스
 */
data class RemovedData(
    val deletedBullets: List<Bullet>,
    val deletedEnemies: List<Enemy>
    )