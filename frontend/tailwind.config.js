/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    fontFamily: {
      'roboto': ['Roboto', 'sans-serif']
    },
    extend: {
      boxShadow: {
        'gameItem': '0 0 8px 3px rgba(0, 0, 0, 0.10);',
        'gameItemLarge': '0 0 16px 8px rgba(0, 0, 0, 0.10);'
      },
      gridTemplateRows: {
        'bord': 'grid-template-rows: repeat(10, 1fr);',
        '10': 'grid-template-rows: repeat(10, minmax(0, 1fr));'
      },
      gridTemplateColumns: {
        'bord': 'grid-template-columns: repeat(10, 10%);'
      },
      maxHeight: {
        'content': 'max-height: calc(100vh - 64px);'
      }
    },
  },
  plugins: [],
}

