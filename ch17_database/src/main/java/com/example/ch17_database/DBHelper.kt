package com.example.ch17_database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLiteOpenHelper를 상속받는 DB 도우미 클래스 정의
class DBHelper(context: Context) : SQLiteOpenHelper(
    context, // 앱의 컨텍스트
    "testdb", // 데이터베이스 이름
    null,     // 커서 팩토리(null이면 기본 커서 사용)
    1         // 데이터베이스 버전
) {

    // 데이터베이스가 처음 생성될 때 호출되는 콜백
    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블 생성 쿼리 실행
        db?.execSQL(
            """
            create table TODO_TB (
                _id integer primary key autoincrement, -- 자동 증가 ID
                todo text not null                    -- 할 일 내용 (널 불가)
            )
            """.trimIndent()
        )
    }

    // 데이터베이스 버전이 올라갔을 때 호출되는 콜백 (스키마 변경 시 사용)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 현재는 버전 1이라서 구현 필요 없음.
        // 추후 버전 업 시 테이블 삭제 및 재생성 또는 마이그레이션 처리 가능
    }
}