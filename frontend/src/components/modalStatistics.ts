import { Chart, ChartConfiguration, registerables } from 'chart.js';
Chart.register(...registerables);

export default function renderModalStatistics(): HTMLElement {
    const modal = document.createElement("div");
    modal.className = "modal";

    modal.innerHTML = /*html*/`
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>Estatísticas</h2>
            <canvas id="statisticsChart"></canvas>
        </div>
    `;

    // Adiciona o evento de fechar o modal
    const closeButton = modal.querySelector(".close");
    closeButton?.addEventListener("click", () => {
        modal.remove();
    });

    // Adiciona o evento de fechar o modal ao clicar fora dele
    window.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.remove();
        }
    });

    // Dados mockados para o gráfico
    const data = {
        labels: ['Python', 'Java', 'JavaScript', 'C#', 'Ruby'],
        datasets: [{
            label: 'Quantidade de Candidatos',
            data: [10, 15, 8, 5, 2], // Dados mockados
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        }]
    };

    // Configuração do gráfico
    const config: ChartConfiguration<'bar', number[], string> = {
        type: 'bar',
        data: data,
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    };

    // Renderiza o gráfico quando o modal é adicionado ao DOM
    setTimeout(() => {
        const ctx = document.getElementById('statisticsChart') as HTMLCanvasElement;
        new Chart(ctx, config);
    }, 0);

    return modal;
}