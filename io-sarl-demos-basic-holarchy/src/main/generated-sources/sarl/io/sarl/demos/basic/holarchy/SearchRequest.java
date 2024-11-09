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
public class SearchRequest extends Event {
  public final String dirPath;

  public final String searchCriteria;

  public SearchRequest(final String p, final String c) {
    this.dirPath = p;
    this.searchCriteria = c;
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
    SearchRequest other = (SearchRequest) obj;
    if (!Objects.equals(this.dirPath, other.dirPath))
      return false;
    if (!Objects.equals(this.searchCriteria, other.searchCriteria))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.dirPath);
    result = prime * result + Objects.hashCode(this.searchCriteria);
    return result;
  }

  /**
   * Returns a String representation of the SearchRequest event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("dirPath", this.dirPath);
    builder.add("searchCriteria", this.searchCriteria);
  }
}
