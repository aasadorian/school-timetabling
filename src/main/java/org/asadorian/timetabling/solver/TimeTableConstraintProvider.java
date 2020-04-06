package org.asadorian.timetabling.solver;

import org.asadorian.timetabling.domain.Lesson;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                roomConflict(constraintFactory),
                teacherConflict(constraintFactory),
                studentGroupConflict(constraintFactory),
                teacherTimeEfficiency(constraintFactory)
        };
    }

    private Constraint teacherTimeEfficiency (ConstraintFactory constraintFactory) {
//        return constraintFactory.from(Lesson.class).join(Lesson.class,
//                Joiners.equal(Lesson::getTeacher),
//                Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
//                .filter((lesson1, lesson2) -> lesson1.getTimeslot().getEndTime().equals(lesson2.getTimeslot().getStartTime()))
//                .reward("Teacher time efficiency", HardSoftScore.ONE_SOFT);

        return constraintFactory.from(Lesson.class).join(Lesson.class,
                Joiners.equal(Lesson::getTeacher),
                Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration timeBetween = Duration.between(lesson1.getTimeslot().getEndTime(), lesson2.getTimeslot().getStartTime());
                    return !timeBetween.isNegative() && timeBetween.compareTo(Duration.ofMillis(30)) <= 0;
                })
                .reward("Teacher time efficiency", HardSoftScore.ONE_SOFT);
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Lesson.class).join(Lesson.class, Joiners.lessThan(Lesson::getId), Joiners.equal(Lesson::getTimeslot), Joiners.equal(Lesson::getStudentGroup)).penalize("Student Group conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint teacherConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Lesson.class).join(Lesson.class, Joiners.lessThan(Lesson::getId), Joiners.equal(Lesson::getTimeslot), Joiners.equal(Lesson::getTeacher)).penalize("Teacher conflict", HardSoftScore.ONE_HARD);
    }

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Lesson.class).join(Lesson.class, Joiners.lessThan(Lesson::getId), Joiners.equal(Lesson::getTimeslot), Joiners.equal(Lesson::getRoom)).penalize("Room conflict", HardSoftScore.ONE_HARD);
    }
}
