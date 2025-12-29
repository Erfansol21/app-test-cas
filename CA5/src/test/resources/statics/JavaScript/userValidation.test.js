const { validateUser } = require('./userValidation')

test('valid user input returns true', () => {
    const user = { email: 'test@example.com', password: '123456' }
    expect(validateUser(user)).toBe(true)
})

test('invalid email returns false', () => {
    const user = { email: 'invalid-email', password: '123456' }
    expect(validateUser(user)).toBe(false)
})

test('empty input returns false', () => {
    expect(validateUser(null)).toBe(false)
})
