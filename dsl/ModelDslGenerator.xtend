/*
 * generated by Xtext
 */
package de.luma.breakout.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import de.luma.breakout.modelDsl.Entity
import java.net.URI
import de.luma.breakout.modelDsl.modelDsl

/**
 * Generates code from your model files on save.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#TutorialCodeGeneration
 */
class ModelDslGenerator implements IGenerator {
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
//		for(model: resource.allContents.toIterable.filter(modelDsl)) {
//			for (entity : model.entities) {
//				val modelCode = createModelClass(entity, model.namespace);
//				fsa.generateFile(entity.name + ".java", modelCode);
//			}
//		}
	}
	
	def createModelClass(Entity entity, String namespace) { '''
	package «namespace»;
	
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.Id;
	import javax.persistence.Table;
	import javax.persistence.Transient;
	
	import org.codehaus.jackson.annotate.JsonProperty;
	
	@Entity
	@Table(name="«entity.name.toUpperCase()»")
	public class «entity.name» {
		«FOR property:entity.properties»
		@Column(name="«property.name.toUpperCase()»")
		«IF property.isIdentifier»@Id«ENDIF»
		private «property.type» «property.name»;
		«ENDFOR»
		
		@Transient
		private String revision;
		
		«FOR property:entity.properties»
			«IF property.isIdentifier»@JsonProperty("_id")«ENDIF»
			public int get«property.name.toFirstUpper()»() {
				return «property.name»;
			}
		
			«IF property.isIdentifier»@JsonProperty("_id")«ENDIF»
			public void set«property.name.toFirstUpper()»(int highscore) {
				this.«property.name» = «property.name»;
			}
		«ENDFOR»
		
		@Transient @JsonProperty("_rev")
		public String getRevision() {
			return revision;
		}
		@Transient @JsonProperty("_rev")
		public void setRevision(String s) {
			this.revision = s;
		}
	}
	'''
	}
}
