import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { createModal } from "@/utils/modalUtils";
Chart.register(...registerables);

export default function renderModalStatistics(): HTMLElement {
    // Criar o elemento canvas para o gráfico
    const canvasContainer = document.createElement('div');
    
    // Adicionar o canvas ao container
    const canvas = document.createElement('canvas');
    canvas.id = 'statisticsChart';
    canvasContainer.appendChild(canvas);
    
    // Criar o modal com o canvas como conteúdo
    const modal = createModal("Estatísticas", canvasContainer);
    
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