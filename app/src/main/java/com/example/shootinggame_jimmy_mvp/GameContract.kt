package com.example.shootinggame_jimmy_mvp

import com.example.shootinggame_jimmy_mvp.model.ObjectSize
import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.Bullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.Enemy

/** <이 게임 내 View, Presenter 사이의 관계를 나타내는 인터페이스>
 *      말 그대로 View, Presenter 사이의 계약서로, 각 요소가 할 역할이 명시됨.
 *          -View : 모든 데이터를 Presenter 에게서 받아와서 사용자에게 보여줌.
 *          -Presenter : 사용자 입력에 의해 또는 주기적으로 갱신되는 데이터를 View 에게 제공
 */
interface GameContract {
    interface Presenter{
        //게임 시작 시 한 번만 호출
        fun initSet(view: View, displayWidth: Int, displayHeight: Int)

        //사용자 이벤트 처리 과정에서 호출
        fun cannonRotate(progress: Int)
        fun shootBullet()
        fun changeBulletType(type: Type)

        //주기적인 데이터 업데이트 과정에서 호출
        fun periodicUpdateTimerStart()
        fun periodicUpdateTimerStop()
    }

    interface View{
        //게임 시작 시 한 번만 호출
        fun showProperCannon(type: Type)

        //사용자 이벤트 처리 과정에서 호출
        fun showCannonRotation(degree: Float)
        fun showNewBullet(bullet: Bullet?, size: ObjectSize)

        //주기적인 데이터 업데이트 과정에서 호출
        fun showNewEnemy(enemy: Enemy?, size: ObjectSize)
        fun showBulletMoving(bullets: List<Bullet>)
        fun showEnemyMoving(enemies: List<Enemy>)
        fun showUFOMoving(xPos: Float)
        fun showLifeState(life: Int)
        fun removeBullets(bullets: List<Bullet>)
        fun removeEnemies(enemies: List<Enemy>)
        fun showGameEnd()
    }
}