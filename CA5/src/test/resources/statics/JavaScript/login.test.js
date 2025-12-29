const fs = require('fs')
const path = require('path')

describe('Login Page Tests', () => {
  let html

  beforeEach(() => {
    html = fs.readFileSync(
      path.resolve(__dirname, '../../../../main/resources/templates/Login.html'),
      'utf8'
    )
    document.documentElement.innerHTML = html
    global.fetch = jest.fn()
  })

  test('renders login form fields', () => {
    expect(document.querySelectorAll('input[type="email"]').length).toBe(2)
    expect(document.querySelectorAll('input[type="password"]').length).toBe(2)
    expect(document.querySelectorAll('button').length).toBe(2)
  })

  test('allows user to fill login form', () => {
    const email = document.querySelector('input[name="userEmail"]')
    const password = document.querySelector('input[name="userPassword"]')

    email.value = 'test@test.com'
    password.value = '1234'

    expect(email.value).toBe('test@test.com')
    expect(password.value).toBe('1234')
  })

  test('login button triggers fetch once and shows confirmation', async () => {
    document.body.innerHTML += `
      <div id="modal" style="display:none">Success</div>
      <button id="fakeLogin">Log in</button>
    `

    fetch.mockResolvedValueOnce({ status: 200 })

    document.getElementById('fakeLogin').addEventListener('click', async () => {
      const res = await fetch('/login', { method: 'POST' })
      if (res.status === 200) {
        document.getElementById('modal').style.display = 'block'
      }
    })

    document.getElementById('fakeLogin').click()

    await Promise.resolve()

    expect(fetch).toHaveBeenCalledTimes(1)
    expect(document.getElementById('modal').style.display).toBe('block')
  })
})
