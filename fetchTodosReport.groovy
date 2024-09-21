@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
import groovyx.net.http.RESTClient

def todosApiUrl = 'http://jsonplaceholder.typicode.com/todos'
def usersApiUrl = 'http://jsonplaceholder.typicode.com/users'

// Create REST clients
def todosClient = new RESTClient(todosApiUrl)
def usersClient = new RESTClient(usersApiUrl)

// Fetch todos
def todosResponse = todosClient.get([:])
def todos = todosResponse.data

// Fetch users
def usersResponse = usersClient.get([:])
def users = usersResponse.data

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
