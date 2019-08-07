package com.by.blcu.course.dto;

public class CourseModelType {
    /**
	 *INTEGER
	 *id
	 */
    private Integer courseModelTypeId;

    /**
	 *VARCHAR
	 *名称
	 */
    private String name;

    /**
	 *VARCHAR
	 *编码
	 */
    private String code;

    /**
	 *INTEGER
	 *排序
	 */
    private Integer sort;

    /**
	 *TINYINT
	 *状态：0 禁用；1 启用
	 */
    private Byte status;

    public Integer getCourseModelTypeId() {
        return courseModelTypeId;
    }

    public void setCourseModelTypeId(Integer courseModelTypeId) {
        this.courseModelTypeId = courseModelTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", courseModelTypeId=").append(courseModelTypeId);
        sb.append(", name=").append(name);
        sb.append(", code=").append(code);
        sb.append(", sort=").append(sort);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}