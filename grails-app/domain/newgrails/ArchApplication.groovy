package newgrails

class ArchApplication {
    String appName;
    String remark;

    static mapping = {
        remark type: 'text'
    }

    static belongsTo = [user: User]

    static hasMany = [
            stakeholder: Stakeholder,
            quality: Quality,
            decision: Decision,
            alternative: Alternative
    ]

    static constraints = {
        user nullable: true
    }
}
