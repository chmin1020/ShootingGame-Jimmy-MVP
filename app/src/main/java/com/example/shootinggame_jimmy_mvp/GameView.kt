package com.example.shootinggame_jimmy_mvp

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import com.example.shootinggame_jimmy_mvp.databinding.ActivityMainBinding
import com.example.shootinggame_jimmy_mvp.model.ObjectSize
import com.example.shootinggame_jimmy_mvp.model.Type
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.DynamicMovingObject
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.bullet.Bullet
import com.example.shootinggame_jimmy_mvp.model.moving_object.dynamic_moving_object.enemy.Enemy
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 *     GameContract.View 인터페이스를 구현하는 MVP 속 View 역할을 하는 클래스
 *     control 부분이 아닌, view 위치 갱신이나 추가 및 삭제같은 drawing 부분은 이 클래스가 담당한다.
 *     UI 수정을 위해서는 activity context, view 접근 방법이 필요하므로 이를 setting 때 넘겨 받는다.
 */
class GameView: GameContract.View {
    //------------------------------------------------
    //상수 영역
    //

    //이미지 테이블
    companion object ImageTables{
        //대포
        private val cannonImageTable = mapOf(
            Pair(Type.LIGHT_CANNON, R.drawable.light_cannon),
            Pair(Type.NORMAL_CANNON, R.drawable.normal_cannon),
            Pair(Type.HEAVY_CANNON, R.drawable.heavy_cannon)
        )

        //탄환
        private val bulletImageTable = mapOf(
            Pair(Type.LIGHT_BULLET, R.drawable.light_bullet),
            Pair(Type.NORMAL_BULLET, R.drawable.normal_bullet),
            Pair(Type.HEAVY_BULLET, R.drawable.heavy_bullet)
        )

        //적
        private val enemyImageTable = mapOf(
            Pair(Type.LIGHT_ENEMY, R.drawable.light_enemy),
            Pair(Type.NORMAL_ENEMY, R.drawable.normal_enemy),
            Pair(Type.HEAVY_ENEMY, R.drawable.heavy_enemy)
        )
    }

    //탄환과 적 객체 대응 뷰들을 관리하는 테이블
    private val bulletTable = mutableMapOf<Long,ImageView>()
    private val enemyTable = mutableMapOf<Long,ImageView>()


    //실제 UI의 창을 제공하는 MainActivity 와 연결을 위해 존재
    private lateinit var inView: ActivityMainBinding
    private lateinit var context: Context


    private lateinit var presenter: GameContract.Presenter


    //------------------------------------------------
    //함수 영역 (실제 뷰 접근 및 적용을 위한 variables 세팅)
    //

    /* MainActivity context, MainActivity viewBinding 가져옴 */
    fun initSet(context: Context, presenter: GameContract.Presenter): ActivityMainBinding{
        this.context = context
        this.inView = ActivityMainBinding.inflate((context as Activity).layoutInflater)

        this.presenter = presenter

        //터치 입력 관련 리스너 세팅
        initViewListeners()

        return inView
    }

    //------------------------------------------------
    //함수 영역 (사용자가 터치로 입력을 전달할 부분의 listeners 세팅)
    //

    private fun initViewListeners(){
        //대포의 회전 (seekbar.progress -> 0 ~ 100일 때 -90F ~ 90F)
        inView.rotateBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                presenter.cannonRotate(inView.rotateBar.progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        //탄환 발사
        inView.fireButton.setOnClickListener {
            presenter.shootBullet()
        }

        //탄환 타입 변경(light, normal, heavy)
        inView.lightBulletButton.setOnClickListener {
            presenter.changeBulletType(Type.LIGHT_BULLET)
        }
        inView.normalBulletButton.setOnClickListener {
            presenter.changeBulletType(Type.NORMAL_BULLET)
        }
        inView.heavyBulletButton.setOnClickListener {
            presenter.changeBulletType(Type.HEAVY_BULLET)
        }
    }


    //------------------------------------------------
    //함수 영역 (MVP 패턴에서 view 역할을 위한 오버라이딩)
    //

    //최초 게임 실행 시에 실행되는 함수

    /* 대포의 종류(heavy, normal, light)에 따라 이미지를 지정하기 위한 함수 */
    override fun showProperCannon(type: Type){
        inView.cannon.setImageResource(cannonImageTable[type]!!)
    }


    //사용자 입력(listener)에 의해 실행되는 함수들

    /* 대포의 회전 각도를 지정하기 위한 함수 */
    override fun showCannonRotation(degree: Float){
        inView.cannon.rotation = degree
    }

    /* 새로운 탄환을 뷰에 추가하는 함수 */
    override fun showNewBullet(bullet: Bullet?, size: ObjectSize){
        if(bullet != null) //bullet is null -> 생성된 bullet 없음
            bulletTable[bullet.id] = makeNewBulletView(bullet, size)
    }


    //타이머에 의해 동작하는 함수들(runOnUiThread 처리 필요)

    /* 새로운 적을 뷰에 추가하는 함수 */
    override fun showNewEnemy(enemy: Enemy?, size: ObjectSize){
        MainScope().launch {
            if (enemy != null)
                enemyTable[enemy.id] = makeNewEnemyView(enemy, size)
        }
    }

    /* 화면에 있는 탄환 뷰들을 움직이는 함수 */
    override fun showBulletMoving(bullets: List<Bullet>) {
        MainScope().launch {
            eachViewMovingUpdate(bullets, bulletTable)
        }
    }

    /* 화면에 있는 적 뷰들을 움직이는 함수 */
    override fun showEnemyMoving(enemies: List<Enemy>) {
        MainScope().launch {
            eachViewMovingUpdate(enemies, enemyTable)
        }
    }

    /* 적이 나타날 UFO 위치를 바꾸는 함수 */
    override fun showUFOMoving(xPos: Float){
        MainScope().launch {
            inView.enemyUfo.x = xPos
        }
    }

    /* 현재 목숨 개수를 표시하는 함수 */
    override fun showLifeState(life: Int){
        val lifeText = "Life : $life"
        MainScope().launch {
            inView.lifeCount.text = lifeText
        }
    }

    /* 화면 탈출이나 충돌로 사라진 탄환을 뷰에서 제거하는 함수 */
    override fun removeBullets(bullets: List<Bullet>){
        MainScope().launch {
            eachViewRemovingUpdate(bullets, bulletTable)
        }
    }

    /* 화면 탈출이나 충돌로 사라진 적을 뷰에서 제거하는 함수 */
    override fun removeEnemies(enemies: List<Enemy>){
        MainScope().launch {
            eachViewRemovingUpdate(enemies, enemyTable)
        }
    }

    /* 게임 종료를 알리는 텍스트를 띄우는 함수 */
    override fun showGameEnd() {
        MainScope().launch {
            inView.tvGameEnd.visibility = View.VISIBLE
        }
    }


    //------------------------------------------------
    //함수 영역 (UI 적용 프로세스 내부 함수들)
    //

    /* 실제 각 DynamicObject(Bullet, Enemy)를 움직이는 함수 */
    private fun eachViewMovingUpdate(movingObjects: List<DynamicMovingObject>, viewTable: MutableMap<Long, ImageView>){
        val objectInList = movingObjects.iterator()
        var curObj: DynamicMovingObject
        var curView: ImageView?

        //데이터 개수만큼 업데이트 (객체와 대응하는 뷰 각각 가져와서 속성 갱신)

        while (objectInList.hasNext()) {
            curObj = objectInList.next()
            curView = viewTable[curObj.id]
            curView?.x = curObj.x
            curView?.y = curObj.y
        }
    }

    /* 제거되어야 하는 동적 뷰들을 제거하는 함수 */
    private fun eachViewRemovingUpdate(removedObjects: List<DynamicMovingObject>, viewTable: MutableMap<Long, ImageView>){
        val objectInList = removedObjects.iterator()
        var curId: Long

        //id에 해당하는 뷰를 테이블에서 삭제
        while (objectInList.hasNext()) {
            curId = objectInList.next().id
            inView.gameStage.removeView(viewTable[curId])
            viewTable.remove(curId)
        }
    }

    /* 모델에서 새로운 탄환 객체가 만들어진 경우, 이를 뷰로 옮길 때 사용하는 함수 */
    private fun makeNewBulletView(bullet: Bullet, size: ObjectSize): ImageView {
        val newBulletView = ImageView(context)
        newBulletView.setImageResource(bulletImageTable[bullet.type]!!)
        newBulletView.layoutParams = FrameLayout.LayoutParams(size.width, size.height)
        makeViewCommonSetting(bullet, newBulletView)

        //속성 적용이 끝났으면 화면에 추가하고 테이블 추가를 위해 return
        inView.gameStage.addView(newBulletView)
        return newBulletView
    }

    /* 모델에서 새로운 적 객체가 만들어진 경우, 이를 뷰로 옮길 때 사용하는 함수 */
    private fun makeNewEnemyView(enemy: Enemy, size: ObjectSize): ImageView {
        val newEnemyView = ImageView(context)
        newEnemyView.setImageResource(enemyImageTable[enemy.type]!!)
        newEnemyView.layoutParams = FrameLayout.LayoutParams(size.width, size.height)
        makeViewCommonSetting(enemy, newEnemyView)

        //속성 적용이 끝났으면 화면에 추가하고 테이블 추가를 위해 return
        inView.gameStage.addView(newEnemyView)
        return newEnemyView
    }

    /* 새로운 뷰를 만들 때 공통 부분(scaleType, 위치 값) */
    private fun makeViewCommonSetting(obj: DynamicMovingObject, imageView: ImageView){
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.x = obj.x
        imageView.y = obj.y
    }
}