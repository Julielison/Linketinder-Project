package org.linketinder.dao.interfaces

interface ICRUD<T> {
	List<Map<String, Object>> getAll()
	T save(T entity)
	boolean deleteById(Integer id)
	boolean update(T entity)
}
