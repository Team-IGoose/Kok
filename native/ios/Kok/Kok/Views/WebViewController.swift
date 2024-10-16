//
//  ViewController.swift
//  webView
//
//  Created by 이상현 on 10/2/24.
//

import UIKit
import WebKit

class WebViewController: UIViewController, WKNavigationDelegate, WKScriptMessageHandler {
    
    private var webView: WKWebView!
    private var webPage: WebPage?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupWebView()
        loadWebPage()
        updateBackgroundColor()
    }
    
    private func setupWebView() {
        let config = WKWebViewConfiguration()
        config.allowsInlineMediaPlayback = true
        config.userContentController = setContentController()
        
        webView = WKWebView(frame: .zero, configuration: config)
        webView.navigationDelegate = self
        webView.scrollView.bounces = false
        

        // 확대/축소 막기 **작동 안하는듯**
        webView.scrollView.minimumZoomScale = 1.0
        webView.scrollView.maximumZoomScale = 1.0
        
        // User Agent 수정
        webView.customUserAgent = "IosApp/1.0"
        
        self.view.addSubview(webView)

        webView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            webView.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor),
            webView.bottomAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.bottomAnchor),
            webView.leadingAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.leadingAnchor),
            webView.trailingAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.trailingAnchor)
        ])
    }
    
    private func loadWebPage() {
        guard let urlString = webPage?.url, let url = URL(string: urlString) else {
            return
        }
        let request = URLRequest(url: url)
        webView.load(request)
    }
    
    //페이지 로드가 끝나면 js에서 정의된 getToken(token)함수를 실행 시킴
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        if let token = TokenModel.shared.getToken(){
            guard let tokenData = try? JSONEncoder().encode(token),
                let tokenString = String(data: tokenData, encoding: .utf8) else {
                print("토큰 인코딩 실패")
                return
            }
            webView.evaluateJavaScript("getToken(\(tokenString))", completionHandler: { (result, error) in
                if let error = error {
                    print("JavaScript error: \(error)")
                } else {
                    print("JavaScript result: \(String(describing: result))")
                }
            })
        }
    }
    
    func setWebPage(_ page: WebPage) {
        self.webPage = page
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        if previousTraitCollection != nil {
            updateBackgroundColor()
        }
    }
    
    private func updateBackgroundColor() {
        if traitCollection.userInterfaceStyle == .dark {
            self.view.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 1.0)    //0 ~ 1 사이 값 0.1당 25
        } else {
            self.view.backgroundColor = UIColor(red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0)
        }
    }
    
    private func setContentController() -> WKUserContentController{
        let contentController = WKUserContentController()
        contentController.add(self, name: "auth")
        contentController.add(self, name: "expire")
        contentController.add(self, name: "alertHandler")
        return contentController
    }
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage){
        print(message.name, message.body)
        switch message.name {
        case "auth":
            TokenModel.shared.saveToken((message.body as? String)!)
            break
        case "expire":
            TokenModel.shared.deleteToken()
            break
        case "alertHandler":
            showAlert(message: message.body as! String)
            break
            
        default:
            break
        }
    }
    
    func showAlert(message: String) {
        let alert = UIAlertController(title: "Alert", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        present(alert, animated: true, completion: nil)
    }
}

extension AppDelegate {
    func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
        // 회전 비활성화 (세로 모드 고정)
        return .portrait
    }
}
