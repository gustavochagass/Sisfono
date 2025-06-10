document.addEventListener('DOMContentLoaded', function() {
      const itemsPerPage = 5;
      const paginationContainer = document.querySelector('.pagination-container');
      const tableBody = document.querySelector('.table-container tbody');

      if (tableBody && paginationContainer) {
        const rowCount = tableBody.querySelectorAll('tr').length;
        if (rowCount > itemsPerPage) {
          paginationContainer.style.display = 'flex';
        } else {
          paginationContainer.style.display = 'none';
        }
      } else {
        if (!tableBody) console.warn('Elemento tbody da tabela não encontrado.');
        if (!paginationContainer) console.warn('Elemento .pagination-container não encontrado.');
      }
    });