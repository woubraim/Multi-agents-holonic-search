package io.sarl.demos.basic.holarchy;

import com.google.common.base.Objects;
import io.sarl.api.core.AgentTask;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.InnerContextAccess;
import io.sarl.api.core.Lifecycle;
import io.sarl.api.core.Logging;
import io.sarl.api.core.MemberLeft;
import io.sarl.api.core.ParticipantJoined;
import io.sarl.api.core.Schedules;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.Scope;
import io.sarl.lang.core.annotation.ImportedCapacityFeature;
import io.sarl.lang.core.annotation.PerceptGuardEvaluator;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.core.util.SerializableProxy;
import java.io.File;
import java.io.ObjectStreamException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class searchagent extends Agent {
  private final ConcurrentSkipListMap<UUID, String> childUIDs = new ConcurrentSkipListMap<UUID, String>();

  private String searchCriteria;

  private UUID idParent;

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName("SearchAgent");
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info("SearchAgent initialized.");
  }

  private void $behaviorUnit$SearchRequest$1(final SearchRequest occurrence) {
    Address _source = occurrence.getSource();
    UUID _iD = this.getID();
    boolean _notEquals = (!Objects.equal(_source, _iD));
    if (_notEquals) {
      this.idParent = occurrence.getSource().getID();
      this.searchCriteria = occurrence.searchCriteria;
      final Path path = Paths.get(occurrence.dirPath);
      final File directory = path.toFile();
      if ((directory.isDirectory() && (!Objects.equal(directory.listFiles(), null)))) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        String _path = directory.getPath();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(((("Searching in directory: " + _path) + " with criteria: ") + this.searchCriteria));
        File[] _listFiles = directory.listFiles();
        for (final File file : _listFiles) {
          boolean _isDirectory = file.isDirectory();
          if (_isDirectory) {
            final UUID aid = UUID.randomUUID();
            this.childUIDs.put(aid, file.getPath());
            Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
            InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
            _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.spawnInContextWithID(searchagent.class, aid, _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext());
            Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
            String _path_1 = file.getPath();
            _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info(("Spawned SearchAgent for sub-directory: " + _path_1));
          } else {
            boolean _contains = file.getPath().contains(this.searchCriteria);
            if (_contains) {
              Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
              String _path_2 = file.getPath();
              _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2.info(("File found: " + _path_2));
              DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
              String _path_3 = file.getPath();
              FileFound _fileFound = new FileFound(_path_3);
              class $SerializableClosureProxy implements Scope<Address> {
                
                private final UUID $_idParent;
                
                public $SerializableClosureProxy(final UUID $_idParent) {
                  this.$_idParent = $_idParent;
                }
                
                @Override
                public boolean matches(final Address it) {
                  UUID _iD = it.getID();
                  return Objects.equal(_iD, $_idParent);
                }
              }
              final Scope<Address> _function = new Scope<Address>() {
                @Override
                public boolean matches(final Address it) {
                  UUID _iD = it.getID();
                  return Objects.equal(_iD, searchagent.this.idParent);
                }
                private Object writeReplace() throws ObjectStreamException {
                  return new SerializableProxy($SerializableClosureProxy.class, searchagent.this.idParent);
                }
              };
              _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_fileFound, _function);
            }
          }
        }
        boolean _isEmpty = this.childUIDs.isEmpty();
        if (_isEmpty) {
          this.completeSearch("No sub-agents required; search finished.");
        }
      } else {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_3 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_3.error(("Invalid or inaccessible directory: " + occurrence.dirPath));
      }
    }
  }

  private void $behaviorUnit$ParticipantJoined$2(final ParticipantJoined occurrence) {
    UUID _contextID = occurrence.spaceID.getContextID();
    InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
    UUID _iD = _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getID();
    boolean _equals = Objects.equal(_contextID, _iD);
    if (_equals) {
      final UUID childID = occurrence.getSource().getID();
      boolean _containsKey = this.childUIDs.containsKey(childID);
      if (_containsKey) {
        final String path = this.childUIDs.get(childID);
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(("Child agent joined for path: " + path));
        InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
        SearchRequest _searchRequest = new SearchRequest(path, this.searchCriteria);
        class $SerializableClosureProxy implements Scope<Address> {
          
          private final Address $_source;
          
          public $SerializableClosureProxy(final Address $_source) {
            this.$_source = $_source;
          }
          
          @Override
          public boolean matches(final Address it) {
            return Objects.equal(it, $_source);
          }
        }
        final Scope<Address> _function = new Scope<Address>() {
          @Override
          public boolean matches(final Address it) {
            Address _source = occurrence.getSource();
            return Objects.equal(it, _source);
          }
          private Object writeReplace() throws ObjectStreamException {
            return new SerializableProxy($SerializableClosureProxy.class, occurrence.getSource());
          }
        };
        _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER_1.getInnerContext().getDefaultSpace().emit(this.getID(), _searchRequest, _function);
      }
    }
  }

  private void $behaviorUnit$FileFound$3(final FileFound occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("Reporting found file to parent agent");
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    FileFound _fileFound = new FileFound(occurrence.fileFoundPath);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_idParent;
      
      public $SerializableClosureProxy(final UUID $_idParent) {
        this.$_idParent = $_idParent;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_idParent);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, searchagent.this.idParent);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, searchagent.this.idParent);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_fileFound, _function);
  }

  private void $behaviorUnit$MemberLeft$4(final MemberLeft occurrence) {
    final UUID childID = occurrence.agentID;
    boolean _containsKey = this.childUIDs.containsKey(childID);
    if (_containsKey) {
      final String path = this.childUIDs.remove(childID);
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      int _size = this.childUIDs.size();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(((("Sub-agent left for path: " + path) + ". Remaining sub-agents: ") + Integer.valueOf(_size)));
      boolean _isEmpty = this.childUIDs.isEmpty();
      if (_isEmpty) {
        this.completeSearch("All sub-agents completed; ending search.");
      }
    }
  }

  protected AgentTask completeSearch(final String message) {
    AgentTask _xblockexpression = null;
    {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(message);
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      SearchFinished _searchFinished = new SearchFinished();
      _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_searchFinished);
      Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
      };
      _xblockexpression = _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(2000, _function);
    }
    return _xblockexpression;
  }

  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE;

  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE);
  }

  @Extension
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES;

  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES);
  }

  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LOGGING;

  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING);
  }

  @Extension
  @ImportedCapacityFeature(InnerContextAccess.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS;

  @SyntheticMember
  @Pure
  private InnerContextAccess $CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS = $getSkill(InnerContextAccess.class);
    }
    return $castSkill(InnerContextAccess.class, this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS);
  }

  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS;

  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS);
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$MemberLeft(final MemberLeft occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$MemberLeft$4(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$FileFound(final FileFound occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$FileFound$3(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchRequest(final SearchRequest occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchRequest$1(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantJoined(final ParticipantJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantJoined$2(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(MemberLeft.class);
    toBeFilled.add(ParticipantJoined.class);
    toBeFilled.add(FileFound.class);
    toBeFilled.add(SearchRequest.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (MemberLeft.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantJoined.class.isAssignableFrom(event)) {
      return true;
    }
    if (FileFound.class.isAssignableFrom(event)) {
      return true;
    }
    if (SearchRequest.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }

  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Object event, final Collection<Runnable> callbacks) {
    super.$evaluateBehaviorGuards(event, callbacks);
    if (event instanceof Initialize) {
      final Initialize occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (event instanceof MemberLeft) {
      final MemberLeft occurrence = (MemberLeft) event;
      $guardEvaluator$MemberLeft(occurrence, callbacks);
    }
    if (event instanceof ParticipantJoined) {
      final ParticipantJoined occurrence = (ParticipantJoined) event;
      $guardEvaluator$ParticipantJoined(occurrence, callbacks);
    }
    if (event instanceof FileFound) {
      final FileFound occurrence = (FileFound) event;
      $guardEvaluator$FileFound(occurrence, callbacks);
    }
    if (event instanceof SearchRequest) {
      final SearchRequest occurrence = (SearchRequest) event;
      $guardEvaluator$SearchRequest(occurrence, callbacks);
    }
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
    searchagent other = (searchagent) obj;
    if (!java.util.Objects.equals(this.searchCriteria, other.searchCriteria))
      return false;
    if (!java.util.Objects.equals(this.idParent, other.idParent))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.searchCriteria);
    result = prime * result + java.util.Objects.hashCode(this.idParent);
    return result;
  }

  @SyntheticMember
  public searchagent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public searchagent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
