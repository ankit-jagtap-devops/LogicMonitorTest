import groovy.json.JsonSlurper

def todosUrl = 'http://jsonplaceholder.typicode.com/todos'
def usersUrl = 'http://jsonplaceholder.typicode.com/users'

// Fetch data from the API
def fetchJson(url) {
    def connection = new URL(url).openConnection()
    connection.setRequestMethod('GET')
    connection.connect()
    new JsonSlurper().parse(connection.inputStream)
}

// Fetch todos and users
def todos = fetchJson(todosUrl)
def users = fetchJson(usersUrl)

// Create a map to hold user data (total todos, completed todos)
def userData = [:].withDefault { [total: 0, completed: 0] }

// Process todos
todos.each { todo ->
    def userId = todo.userId
    userData[userId].total++
    if (todo.completed) {
        userData[userId].completed++
    }
}

// Print report
users.each { user ->
    def userId = user.id
    def totalTodos = userData[userId]?.total ?: 0
    def completedTodos = userData[userId]?.completed ?: 0
    def completionPercentage = totalTodos ? (completedTodos / totalTodos) * 100 : 0
    println "${user.name} has ${totalTodos} todos, ${completedTodos} of which are completed giving him/her a completion percentage of ${completionPercentage.round(2)}%"
}
