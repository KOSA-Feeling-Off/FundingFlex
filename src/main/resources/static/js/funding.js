document.addEventListener('DOMContentLoaded', function () {
    var imageUploadContainer = document.getElementById('imageUploadContainer');
    if (imageUploadContainer) {
        var sortable = new Sortable(imageUploadContainer, {
            animation: 150,
            swap: true,
            swapClass: 'highlight',  // CSS class to add when swapping
            onEnd: function (evt) {
                updateImageSequence();
            }
        });

        // Assign IDs to image upload containers for sortable functionality
        var imageUploads = imageUploadContainer.getElementsByClassName('image-upload');
        for (var i = 0; i < imageUploads.length; i++) {
            imageUploads[i].id = 'image-upload-' + i;
        }
    }
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
        
        if (input.value.trim() === '' || input.value === 0) {
            error.textContent = field.errorMessage;
            error.style.display = 'block';
            isValid = false;
        } else {
            error.style.display = 'none';
        }
    });

    return isValid;
}