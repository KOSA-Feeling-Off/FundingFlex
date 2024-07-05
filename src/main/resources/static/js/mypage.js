document.addEventListener('DOMContentLoaded', function() {
  const tabs = document.querySelectorAll('.tab');
  const contents = document.querySelectorAll('.content');

  function activateTab(tab) {
    // 모든 탭에서 'active' 클래스 제거
    tabs.forEach(t => t.classList.remove('active'));
    // 모든 컨텐츠에서 'con_active' 클래스 제거
    contents.forEach(content => content.classList.remove('con_active'));

    // 선택된 탭에 클래스 추가
    tab.classList.add('active');
    const targetId = tab.getAttribute('data-target');
    document.getElementById(targetId).classList.add('con_active');
  }

  // 각 탭에 클릭 이벤트 리스너 설정
  tabs.forEach(tab => {
    tab.addEventListener('click', () => activateTab(tab));
  });

  // 초기 활성화 탭 설정 (첫 번째 탭 활성화)
  if (tabs.length > 0) {
    activateTab(tabs[0]);
  }
});