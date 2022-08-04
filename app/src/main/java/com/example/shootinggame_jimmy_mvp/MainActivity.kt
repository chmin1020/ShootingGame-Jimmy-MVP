package com.example.shootinggame_jimmy_mvp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shootinggame_jimmy_mvp.databinding.ActivityMainBinding
import com.example.shootinggame_jimmy_mvp.model.*

/**
 *     액티비티는 MainContract.View, MainContract.Presenter 사이를 연결하는 컴포넌트
 *     리스너 세팅을 마친 뷰 바인딩 객체와 컨텍스트로 view 세팅을 완료하고,
 *     이 뷰와 게임화면 크기로 presenter 세팅을 완료하여 둘을 연결한다.
 *     UI와 깊은 관련이 있지만 listener 지정이나 터치 활성화 같은 컨트롤 부분만 담당한다.
 */
class MainActivity : AppCompatActivity() {
    //------------------------------------------------
    //상수 영역
    //

    //view & presenter
    private val gameView = GameView()
    private val gamePresenter = GamePresenter()

    //------------------------------------------------
    //생명 주기 함수
    //

    /* onCreate()에서는 view binding, presenter, listeners 세팅 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding 객체 세팅 및 화면 적용
        setContentView(gameView.initSet(this, gamePresenter).root)

        //gameView, gamePresenter 세팅
        gamePresenter.initSet(gameView, resources.displayMetrics.widthPixels,
                            (0.7F * resources.displayMetrics.heightPixels).toInt())
    }

    /* onResume()에서는 주기적 갱신을 위한 타이머를 설정하고 뷰를 사용가능하게 바꿈 */
    override fun onResume() {
        super.onResume()
        gameResume() //게임 시작
    }

    /* onPause()에서는 게임을 잠깐 정지(타이머 취소 및 뷰를 사용 불가능하게) */
    override fun onPause() {
        super.onPause()
        gamePause() //게임 중지
    }


    //------------------------------------------------
    //함수 영역 (게임 실행 및 종료와 연관됨)
    //

    /* onResume 상황에서 gameEnd 상황이 아니라면 다시 게임 활성화 */
    private fun gameResume(){
        //타이머 재실행과 컨트롤러 활성화
        gamePresenter.periodicUpdateTimerStart()
    }

    /* onPause 에서 게임이 중지됨 */
    private fun gamePause(){
        //타이머 동작 취소와 컨트롤러 비활성화
        gamePresenter.periodicUpdateTimerStop()
    }
}