import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { createModal } from "@/utils/modalUtils";
import { mockCandidates } from "../dataMocked/candidatesMock"; // Alterado para importar candidatos
Chart.register(...registerables);

export default function renderModalStatistics(): HTMLElement {
    // Criar o elemento canvas para o gráfico
    const canvasContainer = document.createElement('div');
    canvasContainer.id = 'statisticsChartContainer';
    
    // Adicionar o canvas ao container
    const canvas = document.createElement('canvas');
    canvas.id = 'statisticsChart';
    canvasContainer.appendChild(canvas);
    
    // Criar o modal com o canvas como conteúdo
    const modal = createModal("Estatísticas de Habilidades", canvasContainer);
    
    // Processar os dados dos candidatos para obter estatísticas de habilidades
    const skillsCounts: Record<string, number> = {};
    
    // Contar ocorrências de cada habilidade nos candidatos
    mockCandidates.forEach(candidate => {
        candidate.skills.forEach(skill => {
            if (skillsCounts[skill]) {
                skillsCounts[skill]++;
            } else {
                skillsCounts[skill] = 1;
            }
        });
    });
    
    // Ordenar as habilidades por contagem (do maior para o menor)
    const sortedSkills = Object.entries(skillsCounts)
        .sort((a, b) => b[1] - a[1])
    
    // Extrair rótulos e dados para o gráfico
    const labels = sortedSkills.map(entry => entry[0]);
    const counts = sortedSkills.map(entry => entry[1]);
    
    // Dados para o gráfico baseados nos candidatos
    const data = {
        labels: labels,
        datasets: [{
            label: 'Habilidades dos candidatos',
            data: counts,
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
        }]
    };

    // Configuração do gráfico
    const config: ChartConfiguration<'bar', number[], string> = {
        type: 'bar',
        data: data,
        options: {

            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            if (Number.isInteger(value)) {
                                return value;
                            }
                            return null;
                        }
                    }
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: 'Distribuição de Habilidades entre Candidatos'
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