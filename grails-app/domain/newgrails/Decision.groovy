package newgrails

class Decision {
    Integer weight;

    static belongsTo = [app : ArchApplication, stakeholder : Stakeholder, quality: Quality]

    static hasMany = [alternative: Alternative]

    static constraints = {
    }
}
