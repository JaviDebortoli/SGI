package SGI.domain;

import java.util.Set;

public enum IssueStatus {
    OPEN {
        @Override
        public Set<IssueStatus> allowedTransitions() {
            return Set.of(IN_PROGRESS, BLOCKED);
        }
    },
    IN_PROGRESS {
        @Override
        public Set<IssueStatus> allowedTransitions() {
            return Set.of(BLOCKED, DONE);
        }
    },
    BLOCKED {
        @Override
        public Set<IssueStatus> allowedTransitions() {
            return Set.of(IN_PROGRESS, DONE);
        }
    },
    DONE {
        @Override
        public Set<IssueStatus> allowedTransitions() {
            return Set.of();
        }
    };

    public abstract Set<IssueStatus> allowedTransitions();

    public boolean canTransitionTo(IssueStatus target) {
        return allowedTransitions().contains(target);
    }
}