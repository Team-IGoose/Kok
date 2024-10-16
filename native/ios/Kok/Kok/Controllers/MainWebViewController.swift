//
//  MainWebViewController.swift
//  webView
//
//  Created by 이상현 on 10/8/24.
//

import UIKit

class MainWebViewController: UIViewController {
    
    private var webViewController: WebViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupWebViewController()
    }
    
    private func setupWebViewController() {
        webViewController = WebViewController()
        
        let webPage = WebPage(url: "http://192.168.1.110:3000")
        webViewController.setWebPage(webPage)
        
        add(webViewController)
    }
    
    private func add(_ child: UIViewController) {
        addChild(child)
        view.addSubview(child.view)
        child.view.frame = view.bounds
        child.didMove(toParent: self)
    }
}
