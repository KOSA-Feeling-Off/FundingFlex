//const socket = new WebSocket('ws://192.168.230.13/ws/chat');
const socket = new WebSocket('ws://localhost/ws/chat');
const chatForm = document.getElementById('chat-form');
const chatBox = document.getElementById('chat-box');
const chatInput = document.getElementById('chat-input');
const noticeBar = document.querySelector('.notice-bar');

// 오늘의 날짜를 표시
document.addEventListener('DOMContentLoaded', () => {
    const dateLine = document.getElementById('date-line').querySelector('time');
    const today = new Date();
    dateLine.textContent = `${today.getFullYear()}년 ${today.getMonth() + 1}월 ${today.getDate()}일 ${today.toLocaleDateString('ko-KR', { weekday: 'long' })}`;
    dateLine.setAttribute('datetime', today.toISOString().split('T')[0]);
});

socket.onopen = function() {
    console.log('WebSocket 연결 성공');
    noticeBar.innerText = "상담원과 연결중입니다...";
    noticeBar.style.display = 'block';
    socket.send("REQUEST_CHAT");
};

socket.onerror = function(error) {
    console.error('WebSocket 오류:', error);
};

socket.onmessage = function(event) {
    const time = new Date();
    const formattedTime = time.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
    const lastMessage = chatBox.querySelector('.friend-chat:last-of-type');
    const shouldShowProfile = !lastMessage || lastMessage.querySelector('time').textContent !== formattedTime;

    if (event.data === "CONNECTED") {
        noticeBar.innerText = "상담원과 연결되었습니다.";
    } 
    else if(event.data === "DISCONNECTED"){
		noticeBar.innerText = "상담원과 연결이 종료되었습니다.";
	}
    else if (event.data === "WAITING") {
        noticeBar.innerText = "상담원을 기다리고 있습니다...";
        noticeBar.style.display = 'block';
    } else {
        chatBox.innerHTML += `
            <div class="friend-chat">
                ${shouldShowProfile ? '<img class="profile-img" src="/images/chat/pic/agent.png" alt="상담사">' : '<img class="profile-img" src="/images/chat/pic/no-image.png" alt="상담사">'}
                <div class="friend-chat-col">
                    ${shouldShowProfile ? '<span class="profile-name">상담원</span>' : ''}
                    <span class="balloon">${event.data}</span>
                </div>
                <time datetime="${formattedTime}">${formattedTime}</time>
            </div>`;
        chatBox.scrollTop = chatBox.scrollHeight;  // 스크롤을 아래로 이동
    }
};

chatForm.addEventListener('submit', function(event) {
    event.preventDefault();
    const message = chatInput.value;
    const time = new Date();
    const formattedTime = time.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
    const lastMessageTime = chatBox.querySelector('.me-chat:last-of-type time');
    if (lastMessageTime && lastMessageTime.textContent === formattedTime) {
        lastMessageTime.style.display = 'none';
    }
    chatBox.innerHTML += `
        <div class="me-chat">
            <div class="me-chat-col">
                <span class="balloon">${message}</span>
            </div>
            <time datetime="${formattedTime}">${formattedTime}</time>
        </div>`;
    socket.send(message);
    chatInput.value = '';
    chatBox.scrollTop = chatBox.scrollHeight;  // 스크롤을 아래로 이동
});
