function validateUser(user) {
    if (!user) return false
    if (!user.email || !user.password) return false
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return emailRegex.test(user.email) && user.password.length >= 6
}

module.exports = { validateUser }
