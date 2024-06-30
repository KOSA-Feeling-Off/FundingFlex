document.addEventListener('DOMContentLoaded', function () {
    var imageUploadContainer = document.getElementById('imageUploadContainer');
    if (imageUploadContainer) {
        var sortable = new Sortable(imageUploadContainer, {
            animation: 150,
            swap: true,
            swapClass: 'highlight',
            onEnd: function (evt) {
                updateImageSequence();
            }
        });

        
        var imageUploads = imageUploadContainer.getElementsByClassName('image-upload');
        for (var i = 0; i < imageUploads.length; i++) {
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
    heartButton.addEventListener('click', () => {
        heartButton.classList.toggle('clicked');
    });
});


function previewImage(input) {
    var file = input.files[0];
    var maxSize = 2 * 1024 * 1024; // 2MB

    if (file.size > maxSize) {
        alert('이미지 파일의 크기는 2MB를 초과할 수 없습니다.');
        input.value = '';
        return;
    }
    
    var reader = new FileReader();

    reader.onload = function(e) {
        var img = input.nextElementSibling;
        img.src = e.target.result;
        img.style.display = 'block';
    }

    reader.readAsDataURL(file);
}

function updateImageSequence() {
    var imageUploadContainer = document.getElementById('imageUploadContainer');
    var imageUploads = imageUploadContainer.getElementsByClassName('image-upload');
    var sequence = [];
	
    for (var i = 0; i < imageUploads.length; i++) {
        var input = imageUploads[i].querySelector('input[type="file"]');
        sequence.push(input.files[0] ? input.files[0].name : null);
    }
	
    console.log('Image sequence updated:', sequence);
}


/*
	유효성 검사
*/
function validateForm() {
    var fields = [
		{ id: 'category', errorId: 'categoryError', errorMessage: '카테고리를 선택하세요' },
        { id: 'title', errorId: 'titleError', errorMessage: '제목을 입력해주세요' },
        { id: 'content', errorId: 'contentError', errorMessage: '내용을 입력해주세요' },
        { id: 'goalAmount', errorId: 'amountError', errorMessage: '금액을 입력해주세요' }
    ];

    var isValid = true;

    fields.forEach(function(field) {
        var input = document.getElementById(field.id);
        var error = document.getElementById(field.errorId);

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

// 이미지 선택 이벤트
function changeMainImage(thumbnail) {
    const mainImage = document.getElementById('mainImage');
    mainImage.src = thumbnail.src;
}