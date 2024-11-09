package io.sarl.demos.basic.holarchy;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.Objects;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class FileFound extends Event {
  public final String fileFoundPath;

  public FileFound(final String p) {
    this.fileFoundPath = p;
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FileFound other = (FileFound) obj;
    if (!Objects.equals(this.fileFoundPath, other.fileFoundPath))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.fileFoundPath);
    return result;
  }

  /**
   * Returns a String representation of the FileFound event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("fileFoundPath", this.fileFoundPath);
  }
}
