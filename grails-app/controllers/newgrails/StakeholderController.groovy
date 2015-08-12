package newgrails

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StakeholderController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Stakeholder.list(params), model:[stakeholderCount: Stakeholder.count()]
    }

    def show(Stakeholder stakeholder) {
        respond stakeholder
    }

    def create() {
        respond new Stakeholder(params)
    }

    @Transactional
    def save(Stakeholder stakeholder) {
        if (stakeholder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (stakeholder.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond stakeholder.errors, view:'create'
            return
        }

        stakeholder.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'stakeholder.label', default: 'Stakeholder'), stakeholder.id])
                redirect stakeholder
            }
            '*' { respond stakeholder, [status: CREATED] }
        }
    }

    def edit(Stakeholder stakeholder) {
        respond stakeholder
    }

    @Transactional
    def update(Stakeholder stakeholder) {
        if (stakeholder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (stakeholder.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond stakeholder.errors, view:'edit'
            return
        }

        stakeholder.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'stakeholder.label', default: 'Stakeholder'), stakeholder.id])
                redirect stakeholder
            }
            '*'{ respond stakeholder, [status: OK] }
        }
    }

    @Transactional
    def delete(Stakeholder stakeholder) {

        if (stakeholder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        stakeholder.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'stakeholder.label', default: 'Stakeholder'), stakeholder.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'stakeholder.label', default: 'Stakeholder'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
