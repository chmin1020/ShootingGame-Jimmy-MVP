package com.example.shootinggame_jimmy_mvp

import com.example.shootinggame_jimmy_mvp.model.GameModel
import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.TypeCnt
import java.util.*
import kotlin.concurrent.timer
import kotlin.random.Random

/** <Presenter 역할을 하는 클래스>
 *      Model, View 사이에서 둘 사이의 데이터 교환을 돕는다.
 *      크게 초기 설정, listener 응답, timer 주기라는 3가지 상황에 내부 함수가 실행된다.
 **/
class GamePresenter: GameContract.Presenter {
    //------------------------------------------------
    //상수 영역
    //

    //view, model 과 각각 연결되어 있음
    private lateinit var view: GameContract.View
    private lateinit var model: GameModel

    //모델과 뷰를 주기적으로 갱신하기 위한 타이머
    private lateinit var updateTimer: Timer


    //------------------------------------------------
    //함수 영역 (MVP 패턴에서 presenter 역할을 위한 오버라이딩)
    //

    /* 연결할 뷰와 모델 결정, 뷰(뷰 결정)와 게임 화면 크기(모델 결정)를 인자로 받음 */
    override fun initSet(view: GameContract.View, displayWidth: Int, displayHeight: Int) {
        this.view = view //뷰 연결

        //모델 연결
        val randomCannon = Random(System.currentTimeMillis()).nextInt(TypeCnt.CANNON_TYPE.cnt)
        model = GameModel(displayWidth, displayHeight, randomCannon)
        view.showProperCannon(model.cannonType)
    }


    /* 대포 회전 데이터를 위한 상호 전달 */
    override fun cannonRotate(progress: Int) {
        view.showCannonRotation(model.cannonRotate(progress))
    }

    /* 새 탄환 생성을 위한 상호 전달 */
    override fun shootBullet() {
        view.showNewBullet(model.shootBullet(), model.bulletSize)
    }

    /* 탄환의 새로운 타입을 모델에게 전달 */
    override fun changeBulletType(type: Type) {
        model.changeBulletType(type)
    }


    /* 주기적으로 나타나는 모델과 뷰 사이 데이터 갱신 및 이동을 위한 타이머 실행 */
    override fun periodicUpdateTimerStart() {
        updateTimer = timer(period = model.updatePeriod) {
            updateDataAboutNewEnemy() //게임에 적 추가
            updateDataAboutObjectsMoving() //시간에 따른 개체들의 움직임
            updateDataAboutObjectsRemoving() //충돌이나 화면 벗어남으로 인한 개체 제거
            updateDataAboutLife() //목숨의 개수와 이에 따른 게임 종료 여부
        }
    }

    /* 업데이트 타이머 종료 */
    override fun periodicUpdateTimerStop() {
        updateTimer.cancel()
    }


    //------------------------------------------------
    //함수 영역 (주기적 갱신 시 실행되는 함수들)
    //

    /* 뷰의 적 추가 데이터 전달 */
    private fun updateDataAboutNewEnemy(){
        val newEnemy = model.dataAddEnemyUpdate() //모델 업데이트
        view.showNewEnemy(newEnemy, model.enemySize) //뷰 적용
    }

    /* 뷰에 움직임 데이터 전달 */
    private fun updateDataAboutObjectsMoving(){
        model.dataMoveUpdate() //모델 업데이트

        //뷰 적용
        view.showUFOMoving(model.getUFOPos())
        view.showBulletMoving(model.getBullets())
        view.showEnemyMoving(model.getEnemies())
    }

    /* 뷰에 삭제 데이터 전달 */
    private fun updateDataAboutObjectsRemoving(){
        val removedData = model.dataRemoveUpdate() //모델 업데이트

        //뷰 적용
        view.removeBullets(removedData.deletedBullets)
        view.removeEnemies(removedData.deletedEnemies)
    }

    /* 목숨 개수 데이터 전달 및 게임 종료 결정 */
    private fun updateDataAboutLife(){
        val life = model.getLife() //모델 이미 업데이트 됨(by enemy removing)

        //뷰 적용
        view.showLifeState(life)

        //life 값이 0 이하면 게임 종료를 알리는 뷰 적용
        if(life <= 0)
            view.showGameEnd()
    }
}