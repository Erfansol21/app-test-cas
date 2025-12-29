const fs = require('fs');
const path = require('path');

describe('Login page snapshot', () => {
  test('Login.html should match snapshot', () => {
    const htmlPath = path.resolve(
      __dirname,
      '../../../../main/resources/templates/Login.html'
    );

    const html = fs.readFileSync(htmlPath, 'utf8');

    document.documentElement.innerHTML = html;

    expect(document.documentElement).toMatchSnapshot();
  });
});
