package newgrails

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class DecisionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Decision.list(params), model:[decisionCount: Decision.count()]
    }

    def show(Decision decision) {
        respond decision
    }

    def create() {
        respond new Decision(params)
    }

    @Transactional
    def save(Decision decision) {
        if (decision == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (decision.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond decision.errors, view:'create'
            return
        }

        decision.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'decision.label', default: 'Decision'), decision.id])
                redirect decision
            }
            '*' { respond decision, [status: CREATED] }
        }
    }

    def edit(Decision decision) {
        respond decision
    }

    @Transactional
    def update(Decision decision) {
        if (decision == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (decision.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond decision.errors, view:'edit'
            return
        }

        decision.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'decision.label', default: 'Decision'), decision.id])
                redirect decision
            }
            '*'{ respond decision, [status: OK] }
        }
    }

    @Transactional
    def delete(Decision decision) {

        if (decision == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        decision.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'decision.label', default: 'Decision'), decision.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'decision.label', default: 'Decision'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
