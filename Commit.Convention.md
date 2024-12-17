# 설명

- 협업 간 커밋 스타일을 일관되게 유지하고 프로젝트 버전 관리를 용이하게 하기 위해 본 컨벤션을 작성하였다.

# 커밋 작성 원칙

- 커밋 메세지는 다음과 같은 형식과 규칙을 따른다.  
  `[Icon]<type>: #{Issue 번호} <subject>`  
  `<body>`  
  `<BLANK LINE>`  
  `<footer>`

- 작업 완료 후 `full request` 커밋 시 #{Issue 번호} 앞에 `Closes`를 붙여 이슈를 클로징한다.

- `Type` 유형은 아래와 같다.

# 커밋 메시지 타입 (Icon - Type)

💎 New Release: release  
🎉 Initial Commit: init  
✨ 새로운 기능을 추가할 때: feat  
🎨 코드의 형식 / 구조를 개선할 때: style  
📰 새 파일을 만들 때: file  
📝 사소한 코드 또는 언어를 변경할 때: chore  
📚 문서를 쓸 때: docs  
🐛 버그를 수정할 때: fix  
🚑 긴급 버그 수정 (Hotfix)일 때: hotfix  
🔥 코드 또는 파일 제거할 때, @CHANGED 주석 태그와 함께: remove  
🚜 파일 구조를 변경할 때, 🎨과 함께 사용: structure  
🔨 코드를 리팩토링할 때: refactor  
💄 UI/style 개선시: ui  
☔️ 테스트를 추가할 때: test  
🔒 보안을 다룰 때: security  
⚙️ 설정 파일을 변경할 때: config  
🌍 다국어 지원 추가/수정: i18n  
🐎 성능을 향상시킬 때: perf  
🐧 리눅스에서 무언가를 고칠 때: linux  
🍎 Mac OS에서 무언가를 고칠 때: mac  
🏁 Windows에서 무언가를 고칠 때: windows  
🔬 코드 범위를 추가할 때: coverage  
💚 CI 빌드를 고칠 때: ci  
⬆️ 종속성을 업그레이드할 때: upgrade  
⬇️ 종속성을 다운그레이드할 때: downgrade  
⏩ 이전 버전/지점에서 기능을 전달할 때: forwardport  
⏪ 최신 버전/지점에서 기능을 백포트할 때: backport  
👕 linter/strict/deprecation 경고를 제거할 때: lint  
♿️ 접근성을 향상시킬 때: accessibility  
🚧 WIP (진행 중인 작업)에 커밋, @REVIEW 주석 태그와 함께 사용: wip  
🔖 버전 태그: tag  
🔈 로깅을 추가할 때: log  
🔇 로깅을 줄일 때: silent  
⚡️ 도입할 때 이전 버전과 호환되지 않는 특징, @CHANGED 주석 태그 사용: breaking  
💡 새로운 아이디어, @IDEA 주석 태그: idea  
🚀 배포/개발 작업과 관련된 모든 것: deploy  
🐳 도커 구성: docker  
🤝 파일을 병합할 때: merge  
🛠️ 빌드 시스템이나 외부 종속성과 관련된 변경: build  
📊 분석 기능 추가/수정: analytics  
🌱 초기 데이터 seeding 추가/수정: seeds  
📦 종속성을 추가하거나 제거할 때: deps

# Subject (주제)

- 커밋의 목적을 간결하게 설명한다.
- 타입 첫 글자는 대문자로 작성한다.
- 50자 이내로 작성한다.

# Body (본문)

- 커밋의 상세 내용을 작성한다.
- 여러 줄로 작성할 수 있으나 한줄당 72자 이내로 작성한다.
- 양에 구애받지 않고 최대한 상세히 기술한다.
- 어떻게 변경했는지 보다는 무엇을 변경했는지 또는 왜 변경했지는지를 기술한다.
- 선택사항

# Footer (후기)

- 참고한 문서나 추가적인 정보를 작성한다.
- 선택 사항

# 참고 문헌

- 본 컨벤션 전략 구성에 도움을 주신 개발자분에게 감사드립니다.  
  [https://github.com/Team-MMMin/Turn-basedRPG/issues/9](url)  
  [https://hyunjun.kr/21](url)  
  [https://velog.io/@archivvonjang/Git-Commit-Message-Convention](url)
