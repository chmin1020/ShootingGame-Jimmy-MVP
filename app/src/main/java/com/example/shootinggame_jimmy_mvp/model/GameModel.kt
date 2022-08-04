package com.example.shootinggame_jimmy_mvp.model

import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.DynamicMovingObject
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.Bullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.Enemy
import com.example.shootinggame_jimmy_mvp.model.moving_object.UFO
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.BulletFactory
import com.example.shootinggame_jimmy_mvp.model.cannon.CannonFactory
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.EnemyFactory
import kotlin.random.Random

/**
 *     <메인 게임 모델>
 *         고정된 Cannon 1개와 UFO 1개, 숫자가 바뀌는 객체들(Bullet, Enemy)의 리스트 가짐.
 *         Presenter 내부에서 세부 모델들을 알지 못한 상태로 각각을 사용할 수 있도록 도와주는 역할.
 *         데이터 전달, 사용자 요청의 의한 갱신, 자동 갱신 등의 역할에 따라 함수가 구성됨
 */
class GameModel(displayWidth: Int, displayHeight: Int, whichCannon: Int) {
    //------------------------------------------------
    //상수 영역
    //

    //게임 내 객체(Bullet, Enemy)의 위치 변경이 발생하는 빈도
    val updatePeriod = 100L

    //난수로 정해지는 대포와 적의 타입은 배열로 미리 저장해서 사용
    private val cannonTypesList = arrayOf(Type.LIGHT_CANNON, Type.NORMAL_CANNON, Type.HEAVY_CANNON)
    private val enemyTypesList = arrayOf(Type.LIGHT_ENEMY, Type.NORMAL_ENEMY, Type.HEAVY_ENEMY)

    //대포의 종류는 게임시작 시 whichCannon 값을 통해 전달받음
    val cannonType = cannonTypesList[whichCannon]

    //게임 내 개체들의 정보 (가로, 세로, 이미지<생략 가능> 순), 뷰를 그릴 때나 두 객체 겹침 여부 체크할 때 사용함
    val bulletSize = ObjectSize(width = (0.08F * displayWidth).toInt(), height = (0.08F * displayHeight).toInt())
    val enemySize = ObjectSize(width = (0.1F * displayWidth).toInt(), height = (0.1F * displayHeight).toInt())
    private val displaySize = ObjectSize(width = displayWidth, height = displayHeight)

    //게임 화면의 position (화면 벗어남 판별 시 사용)
    private val displayX = 0F
    private val displayY = 0F

    //대포에 관한 데이터 (중심까지의 크기, 중심의 위치)
    //대포의 중심을 기점으로 회전하므로 중심 위주의 데이터가 필요함
    private val cannonHalf = displayHeight * 0.075F
    private val cannonBaseX = displayWidth * 0.5F
    private val cannonBaseY = displayHeight - cannonHalf

    //화면 속 탄환 최대 숫자 (난이도 조절을 위해서는 탄환이 무제한이면 안된다는 판단)
    private val bulletLimit = 3

    //적 생성 주기 관련 상수(최소치, 범위)
    private val leastPeriod = 600L
    private val mostPeriod = 1000L


    //------------------------------------------------
    //변수 영역
    //

    //대포는 처음에 어떤 whichCannon 값이 들어오냐에 따라 어떤 객체가 될지 달라짐
    private val cannon = CannonFactory.create(cannonType)

    //적 생성 위치를 결정하는 포탈
    private val ufo = UFO(displaySize.width - enemySize.width)

    //게임 속 탄환과 적을 저장하는 리스트
    private val bullets =  mutableListOf<Bullet>()
    private val enemies =  mutableListOf<Enemy>()

    //적 개체가 언제 나타날 주기의 기준(enemyPeriod)과 관련 카운터(countForEnemy)
    //enemyPeriod 역시 랜덤하게 변하도록 설정했기 때문에 변수 영역
    private var enemyPeriod = 800L
    private var countTimeForEnemy = 0L

    //현재 모델이 필요 시 만들어야 하는 탄환의 타입 (기본은 normal)
    private var bulletType = Type.NORMAL_BULLET


    //----------------------------------------
    //함수 영역 (변수처럼 사용하지만 호출 때마다 값이 달라지는 것들을 함수로 분리)
    //

    //새로 만들어지는 적과 탄환에게 고유한 아이디를 부여 -> 항상 고유한 현재 시간 사용
    private fun objectId(): Long{
        return System.currentTimeMillis()
    }

    //탄환에 대한 데이터 (탄환의 첫 위치 좌표값 -> cannon 각도에 따라 달라짐)
    //좌표 기준을 중앙으로 맞추기 위하여 bulletWidth/2, bulletHeight/2를 차감
    private fun bulletFirstX(): Float {
        return cannonBaseX + cannon.xVector * cannonHalf - bulletSize.width / 2
    }
    private fun bulletFirstY(): Float {
        return cannonBaseY + cannon.yVector * cannonHalf - bulletSize.height / 2
    }

    //적에 대한 데이터 (적의 랜덤 타입)
    private fun enemyType(): Type {
        return enemyTypesList[Random.nextInt(TypeCnt.ENEMY_TYPE.cnt)]
    }


    //------------------------------------------------
    //함수 영역 (데이터 전달)
    //

    /* 화면에 남아있는 객체들의 리스트 */
    fun getBullets(): List<Bullet> {
        return bullets.toList()
    }
    fun getEnemies(): List<Enemy>{
        return enemies.toList()
    }

    /* 대포에게 남은 목숨 */
    fun getLife(): Int{
        return cannon.life
    }

    /* ufo 위치(x) */
    fun getUFOPos(): Float{
        return ufo.x
    }


    //------------------------------------------------
    //함수 영역 (사용자 입력에 의해 변경되는 데이터 갱신, 리스너에 의해 실행)
    //

    /*  대포 회전 각도 갱신 (각도를 return, 대포 회전 속성으로 사용)
        cannonModel 대포를 progress 만큼 회전할 것을 명령 */
    fun cannonRotate(progress: Int) : Float{
        return cannon.rotate(progress)
    }

    /* 새로운 탄환을 포구에 생성하고 발사 (bullet 데이터 리스트에 값 추가)
         bulletModel 내에 새로운 탄환의 시작 위치와 속도를 보냄  */
    fun shootBullet(): Bullet? {
        if(bulletLimit > bullets.size) {
            val bullet =
                    BulletFactory.create(bulletType, objectId(), bulletFirstX(), bulletFirstY(), cannon.shootVelocityX(), cannon.shootVelocityY())
            bullets.add(bullet)
            return bullet
        }
        return null
    }

    /* 각 탄환 선택 버튼 리스너가 실행될 때마다 발사할 탄환 종류 바뀜 */
    fun changeBulletType(type: Type){
        bulletType = type
    }


    //------------------------------------------------
    //함수 영역 (자동으로 변경되는 데이터 갱신)
    //

    /* 추가되는 데이터(새로운 적)가 있다면 내역을 반환하는 함수 */
    fun dataAddEnemyUpdate(): Enemy?{
        return newEnemy()
    }

    /* 제거되는 데이터(탄환, 적) 내용을 확인하고, 내역을 반환하는 함수 */
    fun dataRemoveUpdate(): RemovedData {
        val removedBullets = mutableListOf<Bullet>()
        val removedEnemies = mutableListOf<Enemy>()

        //화면에서 벗어난 객체와 충돌한 객체 탐색 및 체크
        bulletOutOfLimitCheck(removedBullets)
        enemyOutOfLimitCheck(removedEnemies)
        collisionBetweenBulletAndEnemyCheck(removedBullets, removedEnemies)

        //체크한 객체들 한 번에 제거
        removeCheckedObjects(removedBullets, removedEnemies)

        return RemovedData(removedBullets, removedEnemies)
    }

    /*  ufo 객체와 원래 있던 탄환, 적 객체의 위치를 갱신하는 함수 */
    fun dataMoveUpdate(){
        ufo.positionUpdate()
        eachMoveUpdate(bullets)
        eachMoveUpdate(enemies)
    }


    //------------------------------------------------
    //함수 영역 (데이터 갱신 함수들의 내부 메소드)
    //

    //적 생성(dataAddEnemyUpdate)의 내부 메소드

    /* 시간이 적절하게 지났다면 새 적 개체를 추가하는 함수 */
    private fun newEnemy(): Enemy?{
        //적을 만들 시간이 됐으면 랜덤 위치와 속도를 가진 enemy 생성
        if(isItTimeForNewEnemy()) {
            newEnemyTimeSetting() //타이머 새로 세팅

            val enemy = EnemyFactory.create(enemyType(), objectId(), ufo.x)
            enemies.add(enemy)
            return enemy
        }
        return null
    }

    /* 타이머에 의한 실행 시 마다 따로 시간을 재서 new enemy 등장 타이밍을 정하는 함수
       추가적으로 new enemy 마다 카운팅 시간을 새로 정한다. --> newEnemy()와 연결 */
    private fun isItTimeForNewEnemy(): Boolean{
        //enemy 카운터 갱신, 아직 생성할 때가 아니면 함수 종료
        countTimeForEnemy += updatePeriod
        if(countTimeForEnemy < enemyPeriod)
            return false

        return true
    }

    /* 적을 추가한 뒤 타이머 기준 시간을 새롭게 세팅하는 함수 */
    private fun newEnemyTimeSetting(){
        //enemy 카운터를 초기화하고 목표 시간 다시 랜덤으로 설정(최소 600 ~ 최대 1000ms)
        countTimeForEnemy = 0
        enemyPeriod = Random.nextLong(leastPeriod, mostPeriod)
    }


    //적과 탄환 제거(dataRemoveUpdate)의 내부 메소드

    /* 화면에서 벗어난 탄환을 체크하여 삭제 목록에 추가하는 함수 */
    private fun bulletOutOfLimitCheck(removingBullets: MutableList<Bullet>){
        val bulletInList = bullets.iterator()
        var eachBullet: Bullet

        //모든 bullet 데이터를 순회하며 gameStage 밖에 있는(안 겹치는) 탄환 찾으면 제거
        while(bulletInList.hasNext()){
            eachBullet = bulletInList.next()
            if(!areTheyOverlapped(eachBullet.x, eachBullet.y, bulletSize, displayX, displayY, displaySize))
                removingBullets.add(eachBullet)
        }
    }

    /* 화면에서 벗어난 적을 체크하여 삭제 목록에 추가하는 함수 */
    private fun enemyOutOfLimitCheck(removingEnemies: MutableList<Enemy>){
        val enemyInList = enemies.iterator()
        var eachEnemy: Enemy

        //모든 enemy 데이터를 순회하며 gameStage 밖에 있는(안 겹치는) 적 찾으면 제거
        while(enemyInList.hasNext()){
            eachEnemy = enemyInList.next()
            if(!areTheyOverlapped(eachEnemy.x, eachEnemy.y, enemySize, displayX, displayY, displaySize)) {
                removingEnemies.add(eachEnemy)
                cannon.lifeDecrease() //enemy 탈출 시 life 차감
            }
        }
    }

    /* 적과 탄환의 충돌을 체크하여 삭제 목록에 추가하는 함수 */
    private fun collisionBetweenBulletAndEnemyCheck(removingBullets: MutableList<Bullet>, removingEnemies: MutableList<Enemy>){
        val bulletInList = bullets.iterator()
        var eachBullet: Bullet
        var eachEnemy: Enemy

        //각 탄환마다 모든 적들과 위치 비교
        while(bulletInList.hasNext()){
            eachBullet = bulletInList.next() // 비교할 탄환 선택

            val enemyInList = enemies.iterator()
            while(enemyInList.hasNext()){
                eachEnemy = enemyInList.next() // 비교할 적 선택
                //두 개체가 겹친다면...
                if(areTheyOverlapped(eachBullet.x, eachBullet.y, bulletSize, eachEnemy.x, eachEnemy.y, enemySize)){
                    //탄환은 그냥 제거
                    removingBullets.add(eachBullet)

                    //적은 체력을 줄인 후 죽음을 확인하고 제거
                    eachEnemy.enemyGotDamaged(eachBullet.power)
                    if(eachEnemy.isEnemyDead())
                        removingEnemies.add(eachEnemy)

                    break
                }
            }
        }
    }

    /* 삭제해야 할 리스트의 모든 객체를 삭제하는 작업을 하는 함수 */
    private fun removeCheckedObjects(removingBullets: MutableList<Bullet>, removingEnemies: MutableList<Enemy>){
        //limit, collision 중복 적용된 객체를 처리하고 실제 리스트에서 모두 삭제
        removingBullets.distinct()
        removingEnemies.distinct()
        bullets.removeAll(removingBullets)
        enemies.removeAll(removingEnemies)
    }

    /* 두 개체가 서로 겹치는지 확인하는 함수 */
    private fun areTheyOverlapped(x1: Float, y1:Float, size1: ObjectSize, x2: Float, y2: Float, size2: ObjectSize): Boolean {
        //2번째 개체의 우하단 꼭지점 좌표 계산 (범위 계산을 위해)
        val endX2 = x2 + size2.width
        val endY2 = y2 + size2.height

        //두 개체가 가로 범위에서 겹치는 부분이 있는가?
        if(x1 !in x2..endX2 && x1 + size1.width !in x2..endX2)
            return false

        //두 개체가 세로 범위에서 겹치는 부분이 있는가?
        if(y1 !in y2..endY2 && y1 + size1.height !in y2..endY2)
            return false

        return true
    }


    //적과 탄환 이동(dataMovingUpdate)의 내부 메소드

    /* 화면 속 움직이는 객체들의 (위치)정보 업데이트 */
    private fun eachMoveUpdate(objects: List<DynamicMovingObject>) {
        val it = objects.iterator()
        while (it.hasNext())
            it.next().positionUpdate()
    }
}