import statisticsIcon from '@/assets/img/statistics.svg';
import renderModalStatistics from './modalStatistics';

export default function renderButtonStatistics(): HTMLElement {
    const buttonStatistics = document.createElement("div");
    buttonStatistics.className = "statistics";

    buttonStatistics.innerHTML = /*html*/`
        <button class="statistics-button">
            <img src="${statisticsIcon}" alt="Estatísticas" class="statistics-icon" />
            <span class="statistics-text">Estatísticas</span>
        </button>
    `;

    buttonStatistics.addEventListener("click", () => {

        const modal = renderModalStatistics();
        document.body.appendChild(modal);
    });
    return buttonStatistics;
}