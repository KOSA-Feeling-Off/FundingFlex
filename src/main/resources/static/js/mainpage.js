/*

document.addEventListener('DOMContentLoaded', () => {
    const contentDiv = document.querySelector('[data-page="funding-list"]');
    if (contentDiv) {
        fetchFundings('createdDate');
        fetchFundings('inProgress');
        fetchFundings('likes');
    }
});

async function fetchFundings(sortBy = 'createdDate') {
    try {
        const response = await fetch(`/api/fundings/list?sortBy=${sortBy}`);
        if (response.ok) {
            const fundingsList = await response.json();
            console.log("Fetched Fundings List:", fundingsList); // Debugging statement
            displayFundings(fundingsList, sortBy);
        } else {
            alert('펀딩 목록을 가져오는 중 오류가 발생했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('펀딩 목록을 가져오는 중 오류가 발생했습니다.');
    }
}

function displayFundings(fundingsList, sortBy) {
    const fundingsContainer = document.getElementById(`${sortBy}Container`);
    if (!fundingsContainer) {
        console.error(`Container for ${sortBy} not found`);
        return;
    }
    fundingsContainer.innerHTML = '';

    fundingsList.forEach(funding => {
        const fundingItem = document.createElement('a');
        fundingItem.className = 'funding-item';
        fundingItem.href = `/fundingJoin?fundingsId=${funding.fundingsId}`;

        const title = document.createElement('div');
        title.className = 'funding-title';
        title.textContent = funding.title.length > 20 ? funding.title.substring(0, 20) + '...' : funding.title;

        const details = document.createElement('div');
        details.className = 'funding-details';
        details.innerHTML = `목표 금액: ${funding.goalAmount}원<br> 좋아요 수: <span class="like-count">${funding.likeCount}</span>개`;

        const imagesContainer = document.createElement('div');
        imagesContainer.className = 'funding-images';

        funding.imageUrls.forEach((imageUrl, index) => {
            const imgElement = document.createElement('img');
            imgElement.src = `/images/fundings/${imageUrl}`;
            if (index === 0) {
                imgElement.classList.add('active');
            }
            imagesContainer.appendChild(imgElement);
        });

        const prevButton = document.createElement('a');
        prevButton.className = 'prev';
        prevButton.textContent = '❮';
        prevButton.onclick = function (event) {
            event.preventDefault();
            showSlides(imagesContainer, -1);
        };

        const nextButton = document.createElement('a');
        nextButton.className = 'next';
        nextButton.textContent = '❯';
        nextButton.onclick = function (event) {
            event.preventDefault();
            showSlides(imagesContainer, 1);
        };

        imagesContainer.appendChild(prevButton);
        imagesContainer.appendChild(nextButton);

        const progressContainer = document.createElement('div');
        progressContainer.className = 'funding-progress';

        const progressBar = document.createElement('div');
        progressBar.className = 'funding-progress-bar';
        const progressPercent = (funding.currentAmount / funding.goalAmount) * 100;
        progressBar.style.width = `${progressPercent}%`;

        const progressText = document.createElement('div');
        progressText.className = 'funding-progress-text';
        progressText.textContent = `${Math.round(progressPercent)}%`;

        progressContainer.appendChild(progressBar);
        progressContainer.appendChild(progressText);

        const likeButton = document.createElement('div');
        likeButton.className = 'like-button';
        likeButton.innerHTML = '♥';
        likeButton.onclick = function(event) {
            event.preventDefault();
            handleLike(funding.fundingsId, likeButton, details.querySelector('.like-count'));
        };

        fundingItem.appendChild(title);
        fundingItem.appendChild(details);
        fundingItem.appendChild(imagesContainer);
        fundingItem.appendChild(progressContainer);
        fundingItem.appendChild(likeButton);
        fundingsContainer.appendChild(fundingItem);
    });
}

function showSlides(container, n) {
    const slides = container.querySelectorAll('img');
    let currentIndex = Array.from(slides).findIndex(slide => slide.classList.contains('active'));
    slides[currentIndex].classList.remove('active');
    currentIndex = (currentIndex + n + slides.length) % slides.length;
    slides[currentIndex].classList.add('active');
}

async function handleLike(fundingsId, likeButton, likeCountElement) {
    try {
        const response = await fetch(`/api/fundings/like/${fundingsId}`, {
            method: 'POST',
        });
        if (response.ok) {
            const result = await response.json();
            if (result.liked) {
                likeButton.classList.add('liked');
                likeCountElement.textContent = parseInt(likeCountElement.textContent) + 1;
            } else {
                alert('이미 좋아요를 누르셨습니다.');
            }
        } else {
            alert('좋아요 처리 중 오류가 발생했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('좋아요 처리 중 오류가 발생했습니다.');
    }
}
*/