package com.by.blcu.course.dto;

public class CourseModelRelation {
    /**
	 *INTEGER
	 *主键id
	 */
    private Integer courseModelRelationId;

    /**
	 *INTEGER
	 *课程id
	 */
    private Integer courseId;

    /**
	 *INTEGER
	 *模块id
	 */
    private Integer courseModelType;


    public Integer getCourseModelRelationId() {
        return courseModelRelationId;
    }

    public void setCourseModelRelationId(Integer courseModelRelationId) {
        this.courseModelRelationId = courseModelRelationId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseModelType() {
        return courseModelType;
    }

    public void setCourseModelType(Integer courseModelType) {
        this.courseModelType = courseModelType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", courseModelRelationId=").append(courseModelRelationId);
        sb.append(", courseId=").append(courseId);
        sb.append(", courseModelType=").append(courseModelType);
        sb.append("]");
        return sb.toString();
    }
}