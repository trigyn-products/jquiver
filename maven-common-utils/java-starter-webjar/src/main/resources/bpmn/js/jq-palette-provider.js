function JqPaletteProvider(
    palette,
    create,
    elementFactory,
    translate
) {

    this._create = create;
    this._elementFactory = elementFactory;
    this._translate = translate;

    palette.registerProvider(this);
}

JqPaletteProvider.$inject = [
    "palette",
    "create",
    "elementFactory",
    "translate"
];

JqPaletteProvider.prototype.getPaletteEntries =
function() {

    const create = this._create;
    const elementFactory = this._elementFactory;

    function createAction(
        type,
        group,
        className,
        title
    ) {

        function createListener(event) {

            const shape =
                elementFactory.createShape({
                    type: type
                });

            create.start(
                event,
                shape
            );
        }

        return {

            group: group,

            className: className,

            title: title,

            action: {
                dragstart: createListener,
                click: createListener
            }
        };
    }

    return {

        "create.user-task":
            createAction(
                "bpmn:UserTask",
                "activity",
                "bpmn-icon-user-task",
                "User Task"
            ),

        "create.service-task":
            createAction(
                "bpmn:ServiceTask",
                "activity",
                "bpmn-icon-service-task",
                "Service Task"
            ),

        "create.business-rule-task":
            createAction(
                "bpmn:BusinessRuleTask",
                "activity",
                "bpmn-icon-business-rule-task",
                "Business Rule Task"
            ),

        "create.script-task":
            createAction(
                "bpmn:ScriptTask",
                "activity",
                "bpmn-icon-script-task",
                "Script Task"
            ),

        "create.call-activity":
            createAction(
                "bpmn:CallActivity",
                "activity",
                "bpmn-icon-call-activity",
                "Call Activity"
            ),

        "create.parallel-gateway":
            createAction(
                "bpmn:ParallelGateway",
                "gateway",
                "bpmn-icon-parallel-gateway",
                "Parallel Gateway"
            )
    };
};

window.JqPaletteProvider = {

    __init__: [
        "jqPaletteProvider"
    ],

    jqPaletteProvider: [
        "type",
        JqPaletteProvider
    ]
};