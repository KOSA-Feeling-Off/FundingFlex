document.addEventListener('DOMContentLoaded', function () {

    // 이미지 업로드 컨테이너
    const imageUploadContainer = document.getElementById('imageUploadContainer');

    // 이미지 순서 변경 이벤트 추가
    if (imageUploadContainer) {
        let sortable = new Sortable(imageUploadContainer, {
            animation: 150,
            swap: true,
            swapClass: 'highlight',
            onEnd: function (evt) {
                updateImageSequence();
            }
        });

        let imageUploads = imageUploadContainer.getElementsByClassName('image-upload');
        for (let i = 0; i < imageUploads.length; i++) {
            imageUploads[i].id = 'image-upload-' + i;
        }
    }


	  // 금액 천단위 표시
    function formatNumberWithCommas(number) {
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    const goalAmountElement = document.querySelector('.goal-amount');
    if (goalAmountElement) {
        goalAmountElement.textContent = formatNumberWithCommas(goalAmountElement.textContent);
    }

    const collectedAmountElement = document.querySelector('.collected-amount');
    if (collectedAmountElement) {
        collectedAmountElement.textContent = formatNumberWithCommas(collectedAmountElement.textContent);
    }
	
	  // 애니메이션 효과를 위한 함수
    function animateValue(element, start, end, duration, unit) {
        let startTimestamp = null;
		
        const step = (timestamp) => {
            if (!startTimestamp) startTimestamp = timestamp;
			
            const progress = Math.min((timestamp - startTimestamp) / duration, 1);
            const value = Math.floor(progress * (end - start) + start);
            element.textContent = formatNumberWithCommas(value) + unit;
			
            if (progress < 1) {
                window.requestAnimationFrame(step);
            }
        };
		
        window.requestAnimationFrame(step);
    }

    function animateProgressBar(element, start, end, duration) {
        let startTimestamp = null;
		
        const step = (timestamp) => {
            if (!startTimestamp) startTimestamp = timestamp;
			
            const progress = Math.min((timestamp - startTimestamp) / duration, 1);
            element.style.width = (progress * (end - start) + start) + '%';
            
          if (progress < 1) {
                    window.requestAnimationFrame(step);
                }
            };
		
            window.requestAnimationFrame(step);
    }

    // 퍼센트 애니메이션
    const progressBar = document.getElementById('bar-line');
    const progressLabel = document.getElementById('progress-label');
    const labelRaised = document.getElementById('percent-raised');

    if (progressLabel) {
        const percent = parseInt(progressLabel.textContent.replace('%', '').trim());
        animateValue(progressLabel, 0, percent, 1000, '%');
        animateProgressBar(progressBar, 0, percent, 500);
    }

    if (labelRaised) {
        const percent = parseInt(labelRaised.textContent.replace('%', '').trim());
        animateValue(labelRaised, 0, percent, 1000, '% 달성');
    }
    
    // 금액 애니메이션
    if (collectedAmountElement) {
        const collectedAmountValue = parseInt(collectedAmountElement.textContent.replace(/[^0-9]/g, ''));
        animateValue(collectedAmountElement, 0, collectedAmountValue, 1000, ' 원 달성');
    }
	
	
	  // 좋아요 버튼 클릭 이벤트
    const heartButton = document.getElementById('heartButton');
    if (heartButton) {
        heartButton.addEventListener('click', () => {
            heartButton.classList.toggle('clicked');
        });
    }

});


// 이미지 미리보기
function previewImage(input) {
    let file = input.files[0];

    // 파일이 선택되지 않은 경우
    if (!file) {
        return;
    }

    let maxSize = 2 * 1024 * 1024; // 2MB

    if (file.size > maxSize) {
        alert('이미지 파일의 크기는 2MB를 초과할 수 없습니다.');
        input.value = '';
        return;
    }

    let reader = new FileReader();

    reader.onload = function(e) {
        let img = input.nextElementSibling;
        img.src = e.target.result;
        img.style.display = 'block';
    }

    reader.readAsDataURL(file);
}


// 이미지 순서 변경
function updateImageSequence() {
    let imageUploadContainer = document.getElementById('imageUploadContainer');
    let imageUploads = imageUploadContainer.getElementsByClassName('image-upload');
    let sequence = [];

    for (let i = 0; i < imageUploads.length; i++) {
        let input = imageUploads[i].querySelector('input[type="file"]');
        sequence.push(input.files[0] ? input.files[0].name : null);
    }

    console.log('Image sequence updated:', sequence);
}


/*
	유효성 검사
*/
function validateForm() {
    let fields = [
		{ id: 'category', errorId: 'categoryError', errorMessage: '카테고리를 선택하세요' },
        { id: 'title', errorId: 'titleError', errorMessage: '제목을 입력해주세요' },
        { id: 'content', errorId: 'contentError', errorMessage: '내용을 입력해주세요' },
        { id: 'goalAmount', errorId: 'amountError', errorMessage: '금액을 입력해주세요' }
    ];

    let isValid = true;

    fields.forEach(function(field) {
        let input = document.getElementById(field.id);
        let error = document.getElementById(field.errorId);

        console.log(input);
        if (input.value.trim() === '' || input.value === "0") {
            error.textContent = field.errorMessage;
            error.style.display = 'block';
            isValid = false;

            console.log(field.id);
        } else {
            error.style.display = 'none';
        }
    });
    return isValid;
}

// 펀딩 상세 이미지 선택 이벤트
function changeMainImage(thumbnail) {
    const mainImage = document.getElementById('mainImage');
    mainImage.src = thumbnail.src;
}


	// 펀딩 참여하기 버튼 클릭 이벤트 리스너
    const joinFundingButton = document.getElementById('joinFundingButton');
    if (joinFundingButton) {
        joinFundingButton.addEventListener('click', async function () {
            const fundingId = document.getElementById('fundingId').value; // 적절한 방식으로 펀딩 아이디를 가져옵니다.

            // 로그인 여부 확인 API 요청
            const response = await fetch('/api/auth/check-login', {
                method: 'GET',
                credentials: 'include' // 쿠키를 포함하여 요청
            });

            if (response.status === 401) {
                // 로그인되지 않은 경우 로그인 페이지로 리디렉션
                window.location.href = 'http://localhost/api/signin';
                return;
            }

            const user = await response.json(); // 로그인된 사용자 정보

            const fundingAmount = document.getElementById('fundingAmount').value; // 펀딩 금액을 가져옵니다.
            const nameUndisclosed = document.getElementById('nameUndisclosed').checked ? 'Y' : 'N'; // 이름 비공개 여부
            const amountUndisclosed = document.getElementById('amountUndisclosed').checked ? 'Y' : 'N'; // 금액 비공개 여부

            const requestBody = {
                fundingsId: fundingId,
                userId: user.id,
                fundingAmount: fundingAmount,
                nameUndisclosed: nameUndisclosed,
                amountUndisclosed: amountUndisclosed
            };

            try {
                const response = await fetch('/api/fundings/join', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(requestBody)
                });

                if (response.ok) {
                    alert('펀딩 참여가 완료되었습니다!');
                    // 필요한 경우 페이지를 새로고침하거나 다른 동작을 수행합니다
                } else {
                    const errorText = await response.text();
                    alert('펀딩 참여에 실패했습니다: ' + errorText);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('펀딩 참여 중 오류가 발생했습니다.');
            }
        });
    }
