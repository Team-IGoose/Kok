//
//  TokenModel.swift
//  webView
//
//  Created by 이상현 on 10/7/24.
//

import Foundation

class TokenModel {
    static let shared = TokenModel()
    
    private let tokenKey = "authToken"
    
    private init() {}
    
    // 토큰 저장
    func saveToken(_ token: String) {
        UserDefaults.standard.set(token, forKey: tokenKey)
    }
    
    // 토큰 가져오기
    func getToken() -> String? {
        return UserDefaults.standard.string(forKey: tokenKey)
    }
    
    // 토큰 삭제
    func deleteToken() {
        UserDefaults.standard.removeObject(forKey: tokenKey)
    }
}
